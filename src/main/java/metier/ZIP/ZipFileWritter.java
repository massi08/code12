package metier.ZIP;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Compresser des fichiers dans un metier.ZIP avec ZipOutputStream
 * @author Fobec 2011 (http://www.fobec.com/java/1096/compresser-fichiers-avec-zipoutputstream.html)
 * @author modifié par Audrey Loup
 */
public class ZipFileWritter {

    /**
     * Flux de l'archive zip
     */
    private ZipOutputStream zos;

    /**
     * Constructor: creation d'une nouvelle archive
     * @param zipFile
     * @throws FileNotFoundException
     */
    public ZipFileWritter(String zipFile) throws FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(zipFile);
        //ajout du checksum
        CheckedOutputStream checksum = new CheckedOutputStream(fos, new Adler32());
        this.zos = new ZipOutputStream(new BufferedOutputStream(checksum));
    }

    /**
     * Ajouter un fichier au zip
     * @param fileName
     * @param path = path du projet (qu'on ne veut pas mettre dans l'archive)
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void addFile(String fileName, String path) throws FileNotFoundException, IOException {
        File file = new File(fileName);

        String str= file.getPath().substring(path.length(), file.getPath().length());
        ZipEntry zipEntry=new ZipEntry(str);
        int size = 0;
        byte[] buffer = new byte[1024];
        //Ajouter une entree à l'archive zip
        this.zos.putNextEntry(zipEntry);
        FileInputStream fis = new FileInputStream(fileName);
        //copier et compresser les données
        while ((size = fis.read(buffer, 0, buffer.length)) > 0) {
            this.zos.write(buffer, 0, size);
        }

        this.zos.closeEntry();
        fis.close();
    }

    /**
     * Fermer le fichier zip
     * @throws IOException
     */
    public void close() throws IOException {
        this.zos.close();
    }
}