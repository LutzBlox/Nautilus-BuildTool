package com.lutz.nautilus.cli.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FlagList implements Iterable<Flag> {

	private List<Flag> flags = new ArrayList<Flag>();

	public void addFlag(Flag flag) {

		flags.add(flag);
	}

	public boolean hasFlag(String name) {

		for (Flag f : flags) {

			if (f.getName().equalsIgnoreCase(name)) {

				return true;
			}
		}

		return false;
	}

	public Flag getFlag(String name) {

		for (Flag f : flags) {

			if (f.getName().equalsIgnoreCase(name)) {

				return f;
			}
		}

		return null;
	}

	public Flag[] getFlags() {

		return flags.toArray(new Flag[] {});
	}

	@Override
	public Iterator<Flag> iterator() {

		return flags.iterator();
	}
}
