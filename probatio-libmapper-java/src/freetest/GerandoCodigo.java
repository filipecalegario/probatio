package freetest;

import mvc.model.BlockType;

public class GerandoCodigo {

	public static void main(String[] args) {
		int[] blocks = BlockType.blocks;
		int[] sizes = BlockType.sizeBlocks;
		for (int i = 0; i < blocks.length; i++) {
			System.out.println("case BlockType.BLOCK_" + BlockType.getBlockNameById(blocks[i]) + ":");
			System.out.println("\tresult = new String[" + sizes[i] + "];");
			for (int j = 0; j < sizes[i]; j++) {
				System.out.println("\tresult["+j+"] = \"XXX\";");
			}
			System.out.println("break;");
		}
		for (int i = 0; i < blocks.length; i++) {
			System.out.println("case BlockType.BLOCK_" + BlockType.getBlockNameById(blocks[i]) + ":");
			System.out.println("\tcolorIndex = " + i + ";");
			System.out.println("\tscreenIndex = " + i + ";");
			System.out.println("break;");
		}
	}
}
