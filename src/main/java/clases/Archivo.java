package clases;

public class Archivo {
    private String name;
    private long size;
    private boolean directory;
    private String path;

    public Archivo(String name, String path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.directory = false;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    
    
    @Override
    public String toString() {
        return "Archivo{" + "name=" + name + ", size=" + size + '}';
    }
    
    
}
