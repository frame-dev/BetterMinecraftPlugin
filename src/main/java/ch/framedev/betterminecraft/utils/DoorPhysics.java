package ch.framedev.betterminecraft.utils;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;

public class DoorPhysics {
    private Block getBottomDoorBlock(Block clickedBlock) {
        if (!(clickedBlock.getBlockData() instanceof Door door)) {
            return clickedBlock;
        }

        if (door.getHalf() == Bisected.Half.TOP) {
            return clickedBlock.getRelative(BlockFace.DOWN);
        }
        return clickedBlock;
    }

    private boolean isMatchingDoubleDoor(Block firstBlock, Door firstDoor, Block secondBlock, Door secondDoor) {
        if (firstBlock.equals(secondBlock)) return false;
        if (firstBlock.getType() != secondBlock.getType()) return false;
        if (firstDoor.getHalf() != Bisected.Half.BOTTOM || secondDoor.getHalf() != Bisected.Half.BOTTOM) return false;
        if (firstDoor.getFacing() != secondDoor.getFacing()) return false;
        return firstDoor.getHinge() != secondDoor.getHinge();
    }

    private void setDoorOpen(Block bottomBlock, boolean open) {
        if (!(bottomBlock.getBlockData() instanceof Door bottomDoor)) {
            return;
        }
        bottomDoor.setOpen(open);
        bottomBlock.setBlockData(bottomDoor, false);

        Block topBlock = bottomBlock.getRelative(BlockFace.UP);
        if (topBlock.getBlockData() instanceof Door topDoor) {
            topDoor.setOpen(open);
            topBlock.setBlockData(topDoor, false);
        }
    }

    public void synchroniseGate(Block sourceBlock) {
        if (!(sourceBlock.getBlockData() instanceof Gate)) return;

        BlockFace[] faces = {BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH};

        for (BlockFace face : faces) {
            Block neighbourBlock = sourceBlock.getRelative(face);
            if (!(neighbourBlock.getBlockData() instanceof Gate neighbourGate)) continue;
            neighbourGate.setOpen(sourceBlock.getBlockData() instanceof Gate gate && gate.isOpen());
            neighbourBlock.setBlockData(neighbourGate, false);
        }
    }

    public void synchroniseDoor(Block block) {
        Block sourceBlock = getBottomDoorBlock(block);
        if (!(sourceBlock.getBlockData() instanceof Door sourceDoor)) return;

        BlockFace[] faces = {BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH};

        for (BlockFace face : faces) {
            Block neighbourBlock = block.getRelative(face);
            neighbourBlock = getBottomDoorBlock(neighbourBlock);
            if (!(neighbourBlock.getBlockData() instanceof Door neighbourDoor)) continue;
            if (!isMatchingDoubleDoor(sourceBlock, sourceDoor, neighbourBlock, neighbourDoor)) continue;
            setDoorOpen(neighbourBlock, sourceDoor.isOpen());
            return;
        }
    }
}
