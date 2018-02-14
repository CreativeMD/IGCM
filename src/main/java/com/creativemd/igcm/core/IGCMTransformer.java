package com.creativemd.igcm.core;

import java.util.Iterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.creativemd.creativecore.transformer.CreativeTransformer;
import com.creativemd.creativecore.transformer.Transformer;

public class IGCMTransformer extends CreativeTransformer {

	public IGCMTransformer() {
		super("igcm");
	}

	@Override
	protected void initTransformers() {
		addTransformer(new Transformer("net.minecraft.stats.RecipeBookServer") {
			
			@Override
			public void transform(ClassNode node) {
				MethodNode m = findMethod(node, "add", "(Ljava/util/List;Lnet/minecraft/entity/player/EntityPlayerMP;)V");
				for (Iterator iterator = m.instructions.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = (AbstractInsnNode) iterator.next();
					if(insn instanceof LabelNode)
					{
						insn = insn.getNext();
						m.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
						m.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/creativemd/igcm/core/TransformInteractor", "modifyList", "(Ljava/util/List;)Ljava/util/List;", false));
						m.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ASTORE, 1));
						break;
					}
				}
				m = findMethod(node, "remove", "(Ljava/util/List;Lnet/minecraft/entity/player/EntityPlayerMP;)V");
				for (Iterator iterator = m.instructions.iterator(); iterator.hasNext();) {
					AbstractInsnNode insn = (AbstractInsnNode) iterator.next();
					if(insn instanceof LabelNode)
					{
						insn = insn.getNext();
						m.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 1));
						m.instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/creativemd/igcm/core/TransformInteractor", "modifyList", "(Ljava/util/List;)Ljava/util/List;", false));
						m.instructions.insertBefore(insn, new VarInsnNode(Opcodes.ASTORE, 1));
						break;
					}
				}
			}
		});
	}

}
