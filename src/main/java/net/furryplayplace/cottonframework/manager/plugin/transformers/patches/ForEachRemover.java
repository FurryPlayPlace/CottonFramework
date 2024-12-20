/*
---------------------------------------------------------------------------------
File Name : ForEachRemover

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.manager.plugin.transformers.patches;

import net.furryplayplace.cottonframework.manager.plugin.transformers.ASMUtils;
import net.furryplayplace.cottonframework.manager.plugin.transformers.Transformer;
import net.furryplayplace.cottonframework.manager.plugin.transformers.rebuild.ClassDataProvider;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.List;

public class ForEachRemover extends Transformer {
    public ForEachRemover() {
        super("ForEachRemover");
    }

    @Override
    public byte[] transform(byte[] bytes, ClassDataProvider classDataProvider) {
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            List<AbstractInsnNode> nodes = ASMUtils.findNodes(method.instructions, this::isInvokeDynamicInsnNode);
            nodes.stream()
                    .filter(this::isForEachConsumer)
                    .forEach(node -> transformForEach((InvokeDynamicInsnNode) node, method, classDataProvider));
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);

        return classWriter.toByteArray();
    }

    private boolean isInvokeDynamicInsnNode(AbstractInsnNode node) {
        return node instanceof InvokeDynamicInsnNode invoke &&
                "accept".equals(invoke.name) &&
                "()Ljava/util/function/Consumer;".equals(invoke.desc);
    }

    private boolean isForEachConsumer(AbstractInsnNode node) {
        AbstractInsnNode next = node.getNext();
        return next instanceof MethodInsnNode insn &&
                "forEach".equals(insn.name) && "(Ljava/util/function/Consumer;)V".equals(insn.desc);
    }

    private void transformForEach(InvokeDynamicInsnNode invokeDynamic, MethodNode method, ClassDataProvider classDataProvider) {
        MethodInsnNode methodNode = (MethodInsnNode) invokeDynamic.getNext();

        Handle targetHandle = (Handle) invokeDynamic.bsmArgs[1];
        int targetOpcode = opcodeFromHandle(targetHandle);
        Type targetType = (Type) invokeDynamic.bsmArgs[2];

        int localIterator = allocateLocalVariable(method);

        String collection = methodNode.owner;
        boolean isCollectionInterface = classDataProvider.getClassData(collection).isInterface();

        InsnList patch = createPatchInstructions(targetHandle, targetOpcode, targetType, localIterator, collection, isCollectionInterface);

        method.instructions.insertBefore(invokeDynamic, patch);
        method.instructions.remove(invokeDynamic);
        method.instructions.remove(methodNode);
    }

    private int allocateLocalVariable(MethodNode method) {
        return method.maxLocals++;
    }

    private InsnList createPatchInstructions(Handle targetHandle, int targetOpcode, Type targetType, int localIterator, String collection, boolean isCollectionInterface) {
        InsnList patch = new InsnList();
        LabelNode breakNode = new LabelNode();
        LabelNode continueNode = new LabelNode();

        patch.add(new MethodInsnNode(
                isCollectionInterface ? INVOKEINTERFACE : INVOKEVIRTUAL,
                collection,
                "iterator",
                "()Ljava/util/Iterator;",
                isCollectionInterface
        ));
        patch.add(new VarInsnNode(ASTORE, localIterator));

        patch.add(continueNode);
        patch.add(new FrameNode(F_APPEND, 1, new Object[]{"java/util/Iterator"}, 0, null));
        patch.add(new VarInsnNode(ALOAD, localIterator));
        patch.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true));
        patch.add(new JumpInsnNode(IFEQ, breakNode));

        patch.add(new VarInsnNode(ALOAD, localIterator));
        patch.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true));

        Type[] targetArgs = Type.getMethodType(targetHandle.getDesc()).getArgumentTypes();
        boolean passObject = targetArgs.length == targetType.getArgumentTypes().length;

        patch.add(new TypeInsnNode(CHECKCAST, passObject ? targetArgs[targetArgs.length - 1].getInternalName() : targetHandle.getOwner()));
        patch.add(new MethodInsnNode(targetOpcode, targetHandle.getOwner(), targetHandle.getName(), targetHandle.getDesc(), targetOpcode == INVOKEINTERFACE));

        handleReturnType(patch, targetHandle);

        patch.add(new JumpInsnNode(GOTO, continueNode));
        patch.add(breakNode);
        patch.add(new FrameNode(F_CHOP, 1, null, 0, null));

        return patch;
    }

    private void handleReturnType(InsnList patch, Handle targetHandle) {
        int returnSize = Type.getMethodType(targetHandle.getDesc()).getReturnType().getSize();
        patch.add(new InsnNode(returnSize == 2 ? POP2 : POP));
    }

    public static int opcodeFromHandle(Handle handle) {
        return switch (handle.getTag()) {
            case H_GETFIELD -> GETFIELD;
            case H_GETSTATIC -> GETSTATIC;
            case H_PUTFIELD -> PUTFIELD;
            case H_PUTSTATIC -> PUTSTATIC;
            case H_INVOKEVIRTUAL -> INVOKEVIRTUAL;
            case H_INVOKESTATIC -> INVOKESTATIC;
            case H_INVOKESPECIAL -> INVOKESPECIAL;
            case H_NEWINVOKESPECIAL -> NEW;
            case H_INVOKEINTERFACE -> INVOKEINTERFACE;
            default -> throw new IllegalArgumentException("Unknown handle type: " + handle.getTag());
        };
    }
}