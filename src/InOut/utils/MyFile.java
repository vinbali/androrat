package utils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class MyFile implements Serializable {

	private static final long serialVersionUID = -8076326593527941165L;
	public boolean isFile = false;
	public boolean isDir = true;
	public long lastModif = 0L;
	public long length = 0L;
	public String name;
	public boolean r;
	public boolean w;
	// public boolean x;
	public boolean hidden;
	public String path;
	// private File file;

	public ArrayList<MyFile> list;

	public MyFile(File f) {
		this.name = f.getName();
		this.length = f.length();
		this.lastModif = f.lastModified();
		this.isFile = f.isFile();
		this.isDir = f.isDirectory();
		this.r = f.canRead();
		this.w = f.canWrite();
		// x = f.canExecute(); Not available with Android
		this.hidden = f.isHidden();
		this.path = f.getPath();
		// file = f;
		this.list = new ArrayList<MyFile>();
	}

	public void addChild(MyFile c) {
		this.list.add(c);
	}

	/*
	 * public File getFile() { return file; }
	 */
	public byte[] build() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getLastModif() {
		return this.lastModif;
	}

	public long getLength() {
		return this.length;
	}

	public ArrayList<MyFile> getList() {
		return this.list;
	}

	public String getName() {
		return this.name;
	}

	public String getPath() {
		return this.path;
	}

	public boolean isDir() {
		return this.isDir;
	}

	public boolean isFile() {
		return this.isFile;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public boolean isR() {
		return this.r;
	}

	public boolean isW() {
		return this.w;
	}

	public void parse(byte[] packet) {
		// TODO Auto-generated method stub

	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void setLastModif(long lastModif) {
		this.lastModif = lastModif;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setR(boolean r) {
		this.r = r;
	}

	public void setW(boolean w) {
		this.w = w;
	}
}