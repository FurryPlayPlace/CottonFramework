/*
---------------------------------------------------------------------------------
File Name : Transformer

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.manager.plugin.transformers;

import net.furryplayplace.cottonframework.manager.plugin.transformers.rebuild.ClassDataProvider;
import org.objectweb.asm.Opcodes;

public abstract class Transformer implements Opcodes {
    private final String name;
    public Transformer(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public abstract byte[] transform(byte[] bytes, ClassDataProvider cdp);
}