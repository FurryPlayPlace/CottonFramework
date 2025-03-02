/*
---------------------------------------------------------------------------------
File Name : TransfomerManager

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.manager.plugin.transformers;

import net.furryplayplace.cottonframework.CottonFramework;
import net.furryplayplace.cottonframework.manager.plugin.transformers.patches.ForEachRemover;
import net.furryplayplace.cottonframework.manager.plugin.transformers.rebuild.ClassDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TransformerManager {
    private final Logger logger = LogManager.getLogger("Cotton Plugin Transformation");
    private final List<Transformer> transformers = new CopyOnWriteArrayList<>();

    public TransformerManager() {
        this.register(new ForEachRemover());
    }

    public void register(Transformer transformer) {
        transformers.add(transformer);
    }

    public byte[] transform(String name, byte[] bytes) {
        ClassDataProvider classDataProvider = new ClassDataProvider(CottonFramework.class.getClassLoader());
        classDataProvider.addClasses(Collections.singletonMap(name, bytes));

        for (Transformer transformer : transformers) {
            bytes = transformer.transform(bytes, classDataProvider);

            if (logger.isDebugEnabled()) {
                logger.debug("Applying transformer {} to class {}", transformer.name(), name);
            }
        }

        return bytes;
    }
}