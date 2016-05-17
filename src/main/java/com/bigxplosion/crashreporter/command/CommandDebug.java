package com.bigxplosion.crashreporter.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * This is a debug command and will be OFF on release.
 */
public class CommandDebug extends CommandBase {

	@Override
	public String getCommandName() {
		return "crash";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/crash";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		ObfuscationReflectionHelper.setPrivateValue(World.class, sender.getEntityWorld(), null, "activeChunkSet", "field_72993_I");
	}
}
