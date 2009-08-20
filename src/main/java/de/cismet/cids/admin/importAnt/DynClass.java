/*
 * DynClass.java
 *
 * Created on 3. November 2003, 10:34
 */

package de.cismet.cids.admin.importAnt;
import com.sun.tools.javac.Main;
import java.io.*;

/**
 *
 * @author  hell
 */
public class DynClass extends ClassLoader{
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private String code;
    private File file;
    private byte[] bytecode;
    private String dir;
    private String classname;
    
    
    /** Creates a new instance of DynClass */
    public DynClass(String code,File file,String dir) {
        this.code=code;
        this.file=file;
        this.dir=dir;
    }
    
    
    public void compile(boolean keepSourceAndClass) throws DynamicCompilingException{
        File classfile;
        try {
            String filename=file.getName();
            classname=filename.substring(0,filename.length()-5);
            PrintWriter out=new PrintWriter(new FileOutputStream(file));
            out.println(code);
            out.flush();
            out.close();
            
            com.sun.tools.javac.Main javac=new com.sun.tools.javac.Main(); 
            String[] compArgs=new String[] {"-d",dir,dir+filename};
            ByteArrayOutputStream bErr=new ByteArrayOutputStream();
            System.setErr(new PrintStream(bErr));
            int status=Main.compile(compArgs);
            System.setErr(System.out);
            if (status==0) {
                classfile=new File(file.getParent(),classname+".class");
                classfile.deleteOnExit();
                bytecode=DynClass.getBytesFromFile(classfile);
                if (!keepSourceAndClass){
                    classfile.delete(); //kann auch .deleteOnExit() hin 
                    file.delete(); // wie gew\u00FCnscht
                }
                //log.info("javac hat gefunzt!!");
            } else {
                DynamicCompilingException dce=new DynamicCompilingException(bErr.toString());
                log.error("javac ging schief :-(",dce);
                throw dce;
            }
            
        }
        catch (DynamicCompilingException ex) {
            throw ex;
        }
        catch (Exception ex) {
             throw new DynamicCompilingException("!internal Bug!" + ex.toString());
        }
    }
    
    /** Getter for property code.
     * @return Value of property code.
     *
     */
    public java.lang.String getCode() {
        return code;
    }
    
    /** Setter for property code.
     * @param code New value of property code.
     *
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }
    
    /** Getter for property bytecode.
     * @return Value of property bytecode.
     *
     */
    public byte[] getBytecode() {
        return this.bytecode;
    }
    
    // from http://javaalmanac.com/egs/java.io/File2ByteArray.html
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
    
    
    public Class getTheClass() {
        return defineClass(classname,bytecode,0,bytecode.length);
    }
}


/*
 * // Create a File object on the root of the directory containing the class file
    File file = new File("c:\\myclasses\\");
    
    try {
        // Convert File to a URL
        URL url = file.toURL();          // file:/c:/myclasses/
        URL[] urls = new URL[]{url};
    
        // Create a new class loader with the directory
        ClassLoader cl = new URLClassLoader(urls);
    
        // Load in the class; MyClass.class should be located in
        // the directory file:/c:/myclasses/com/mycompany
        Class cls = cl.loadClass("com.mycompany.MyClass");
    } catch (MalformedURLException e) {
    } catch (ClassNotFoundException e) {
    }*/

