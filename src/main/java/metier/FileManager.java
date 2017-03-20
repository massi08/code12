package metier;

import ViewObjects.Jsons.jstree.Packagetree;
import metier.ZIP.ZipFileWritter;
import dao.FileDAO;
import modele.File;
import modele.Project;
import modele.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.EntityManager;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileManager {
    /**
     * Classe métier responsable de la gestion des fichiers
     */


    private FileDAO fileDAO;
    private EntityManager em;

    private Logger logger = Logger.getLogger(FileManager.class);


    public FileManager(FileDAO fileDAO, EntityManager em) {
        this.fileDAO = fileDAO;
        this.em = em;
    }

    //Construit un metier.ZIP qui est enregistré au niveau du path
    public void ConstructZip(List<File> files, String pathArchive, String pathFichiers, String nomArchive) {
        try {
            ZipFileWritter zip = new ZipFileWritter(pathArchive + "/" + nomArchive);
            for (int i = 0; i < files.size(); i++) {
                zip.addFile(pathFichiers + files.get(i).ConstructPath(), pathArchive);
            }
            zip.close();
        } catch (IOException e) {
            logger.error(e);
            //Logger.getLogger(ZipFileWritter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Création d'un fichier dans la base de donnée
     *
     * @param user
     * @param project
     * @param path
     * @param nameFileF
     * @return
     */
    public File createFile(User user, Project project, String path, String nameFileF) {
        em.getTransaction().begin();
        File f = fileDAO.create(user, project, path, nameFileF);
        em.getTransaction().commit();
        if(project.getGit()){
            gitAdd(project.getIdProject(), f);
        }
        return f;
    }


    /**
     * Enregistrement d'un fichier dans la hiérarchie locale.
     *
     * @param project
     * @param path
     * @param arborescence
     * @param filename
     * @param texte
     * @throws IOException
     */

    public void createLocalFile(Project project, String path, String arborescence, String filename, String texte) throws IOException {
        String strprojectid = Integer.toString(project.getIdProject());
        java.io.File repertoire = new java.io.File(path + "/" + strprojectid + "/" + project.getName() + arborescence);

        if (!repertoire.exists()) {
            if (!repertoire.mkdirs())
                throw new IOException();
        } else if (!repertoire.isDirectory()) { // Il faut vérifier que c'est un répertoire
            throw new IOException();
        }
        FileWriter fw = new FileWriter(path + "/" + strprojectid + "/" + project.getName() + arborescence + filename, false);
        // le BufferedWriter output auquel on donne comme argument le FileWriter fw cree juste au dessus
        BufferedWriter output = new BufferedWriter(fw);
        //on marque dans le fichier ou plutot dans le BufferedWriter qui sert comme un tampon(stream)
        output.write(texte);
        //on peut utiliser plusieurs fois methode write
        output.flush();
        //ensuite flush envoie dans le fichier, ne pas oublier cette methode pour le BufferedWriter
        output.close();
    }

    /**
     * Lecture d'un fichier de la hiérarchie
     *
     * @param path
     * @param file
     * @return
     * @throws IOException
     */
    public String readLocalFile(String path, File file) throws IOException {
        String sCurrentLine;
        BufferedReader br;
        String strrep = "";
        br = new BufferedReader( new InputStreamReader(new FileInputStream(path + file.ConstructPath()), Charset.forName("UTF-8")));
        while ((sCurrentLine = br.readLine()) != null) {
            sCurrentLine=sCurrentLine.replaceAll("\"", "\\\\\\\"" );
            strrep += sCurrentLine + "\\n"; // \n pour json et \\n pour interprété json
        }
        br.close();
        return strrep;
    }

    /**
     * Suppression d'un fichier de la base de données.
     *
     * @param file
     */
    public void deleteFile(File file) {
        em.getTransaction().begin();
        em.remove(file);
        em.getTransaction().commit();
    }

    /**
     * Suppression d'un fichier de la hiérarchie locale.
     *
     * @param path
     * @param file
     * @return
     */
    public boolean deleteLocalFile(String path, File file) {
        java.io.File localfile = new java.io.File(path + file.ConstructPath());
        java.io.File parent = localfile.getParentFile();
        boolean b = localfile.delete();
        this.DeleteUselessDirectories(parent, 0);
        return b;
    }

    /** suppression d'une liste de fichier de la hierarchie
     *
     */

    public boolean deleteLocalFiles(Project p,List<File>  file) {
        String path=p.getPath()+"/"+p.getName()+"/";
        for(int i=0;i<file.size();i++) {
            deleteLocalFile(p.getPath(), file.get(i));

        }
        return true;
    }

    /**
     * Récupération d'un projet par son id
     *
     * @param projectid
     * @return
     */
    public Project getProjectById(int projectid) {
        em.getTransaction().begin();
        Project project = em.find(Project.class, projectid);
        em.getTransaction().commit();
        return project;
    }

    /**
     * Récupération d'un format json résumant l'ensemble des fichiers d'un projet, ordonnés en package
     *
     * @param p
     * @param user
     * @return
     */
    public Packagetree getProjectFiles(Project p, User user) {
        List<File> files=fileDAO.getFileByProject(p);
        Packagetree root=Packagetree.ParseJSONpathfiles(p.getName(), files, user);
        return root ;
    }

    /**
     * Récupération d'un fichier par son ID
     *
     * @param id
     * @return
     */
    public File getFileById(int id) {
        em.getTransaction().begin();
        File file = fileDAO.getbyId(id);
        em.getTransaction().commit();
        return file;
    }

    /**
     * Récupération de tous les fichiers d'un projet.
     *
     * @param p
     * @return
     */
    public List<File> getFileByProject(Project p) {
        return fileDAO.getFileByProject(p);
    }


    /**
     * Permet de changer le propriétaire d'un fichier
     *
     * @param file
     * @param user
     * @return
     */
    public boolean setlocking(File file, User user) {

        em.getTransaction().begin();
        boolean b = file.setLocking(user, user);
        em.getTransaction().commit();
        return b;
    }

    /**
     * Permet de s'enlever comme propriétaire d'un fichier. renvoie true s'il a réussit
     *
     * @param file
     * @param user
     * @return
     */
    public boolean unsetlocking(File file, User user) {
        em.getTransaction().begin();
        boolean b = file.setLocking(user, null);
        em.getTransaction().commit();
        if (b) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Permet de récupérer un fichier de par ses attributs, l'ensemble des attributs est unique
     *
     * @param projectid
     * @param path
     * @param name
     * @return
     */
    public File getFileByAttributes(int projectid, String path, String name) {
        return fileDAO.getFileByAttributes(getProjectById(projectid), path, name);
    }

    /**
     * Modification du nom et de l'emplacement d'un fichier, en base de donnée comme en local.
     *
     * @param file
     * @param path
     * @param filename
     * @param origpath
     * @return
     */
    public boolean ModifyFile(File file, String path, String filename, String origpath) {
        if (getFileByAttributes(file.getIdProjet().getIdProject(), path, filename) != null)
            return false; // Gestion d'un conflit, si qq a créé le même fichier au même endroit depuis la dernière synchronisation
        java.io.File localfile = new java.io.File(origpath + file.ConstructPath());
        Project project = file.getIdProjet();

        java.io.File repertoire = new java.io.File(origpath + "/" + project.getIdProject() + "/" + project.getName() + path);
        if (!repertoire.exists()) {
            if (!repertoire.mkdirs())
                return false;
        }
        boolean success = localfile.renameTo(new java.io.File(origpath + "/" + project.getIdProject() + "/" + project.getName() + path + filename));
        if (success) {
            this.DeleteUselessDirectories(repertoire, 0);
            em.getTransaction().begin();
            file.setNameFile(filename);
            file.setPathFile(path);
            em.getTransaction().commit();
        }
        return success;
    }

    /**
     * On enlève les packages inutiles après avoir renommer un fichier
     *
     * @param file
     * @param compte
     */
    private void DeleteUselessDirectories(java.io.File file, int compte) {
        if (compte == 3) {
            return;
        }
        java.io.File liste[] = file.listFiles();
        if (!file.isDirectory() || liste == null || liste.length != 0) {
            if (liste == null) {
                return;
            }
            return;
        }
        java.io.File parent = file.getParentFile();
        if (file.delete())
            DeleteUselessDirectories(parent, compte + 1);
    }

    public List<File> getFileByUserProject(User user, Project p) {
        return fileDAO.getFileByUserandProject(user, p);
    }

    /**
     * Unzip un dossier et ajoute ses fichiers dans la BD
     *
     * @param zipfile  dossier zip à deziper
     * @param folder dossier où mettre le fichier dézipé
     * @param user auteur des fichiers
     * @param project project des fichiers
     * @param path du serveur à enlever dans les paths des fichiers dans la BD
     * @return
     */
    public void unzip(java.io.File zipfile, java.io.File folder, User user, Project project, String path) throws FileNotFoundException, IOException{
        // création de la ZipInputStream qui va servir à lire les données du fichier zip
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(
                        new FileInputStream(zipfile.getCanonicalFile())));
        // extractions des entrées du fichiers zip (i.e. le contenu du zip)
        ZipEntry ze = null;
        try {
            while((ze = zis.getNextEntry()) != null){
                // Pour chaque entrée, on crée un fichier
                // dans le répertoire de sortie "folder"
                java.io.File f = new java.io.File(folder.getCanonicalPath(), ze.getName());
                // Si l'entrée est un répertoire,
                // on le crée dans le répertoire de sortie
                // et on passe à l'entrée suivante (continue)
                if (ze.isDirectory()) {
                    f.mkdirs();
                    continue;
                }
                // L'entrée est un fichier, on crée une OutputStream
                // pour écrire le contenu du nouveau fichier
                f.getParentFile().mkdirs();
                OutputStream fos = new BufferedOutputStream(
                        new FileOutputStream(f));
                // On écrit le contenu du nouveau fichier
                // qu'on lit à partir de la ZipInputStream
                // au moyen d'un buffer (byte[])
                try {
                    try {
                        final byte[] buf = new byte[8192];
                        int bytesRead;
                        while (-1 != (bytesRead = zis.read(buf)))
                            fos.write(buf, 0, bytesRead);
                    }
                    finally {
                        fos.close();
                    }
                    String str= f.getPath().substring(path.length(), f.getParent().length());
                    createFile(user, project,str+"/", f.getName());
                }
                catch (final IOException ioe) {
                    // en cas d'erreur on efface le fichier
                    fos.close();
                    if(!f.delete())
                        throw ioe;
                    throw ioe;
                }
            }
        }
        finally {
            // fermeture de la ZipInputStream
            zis.close();
        }
    }

    public String gitAdd(int projectid, File file){
        List<String> listForBuilder=new ArrayList<String>();
        listForBuilder.add("git");
        listForBuilder.add("add");
        listForBuilder.add("./"+file.ConstructPathWithoutProjectid());
        return  execCommande(projectid, listForBuilder);
    }


    /**
     * Prépare la commande a executer pour initialiser git "git init" et l'éxecute par l'intermédiaire de "execCommande"
     * @param projectid L'identifian du projet
     * @see FileManager#execCommande(int, List)
     */
    public void gitInit(int projectid){
        List<String> listForBuilder=new ArrayList<String>();
        listForBuilder.add("git");
        listForBuilder.add("init");
        execCommande(projectid, listForBuilder);
    }


    /**
     * Prépare la commande pour le commit et l'execute par l'intermédiaire de "execCommande"
     * @param project Le projet
     * @param files La liste des fichiers à commit
     * @return La sortie de la commande "git commit"
     * @see FileManager#execCommande(int, List)
     */
    public String gitCommit(Project project, List<File> files, String commit_message){
        List<String> listForBuilder=new ArrayList<String>();
        listForBuilder.add("git");
        listForBuilder.add("commit");
        listForBuilder.add("-m");
        listForBuilder.add("\""+commit_message+"\"");
        listForBuilder.add("-o");
        for(File f: files){
            listForBuilder.add("./"+f.ConstructPathWithoutProjectid());
        }

        return  execCommande(project.getIdProject(), listForBuilder);
    }

    /**
     * Prépare la commande pour le "git log" d'un seul fichier et l'execute par l'intermédiaire de "execCommande"
     * @param project Le projet
     * @param file Le fichier sur lequel voir les log
     * @return La sortie de la commande "git log"
     * @see FileManager#execCommande(int, List)
     */
    public String gitLog(Project project, File file){
        List<String> listForBuilder=new ArrayList<String>();
        listForBuilder.add("git");
        listForBuilder.add("log");
        listForBuilder.add("--pretty=oneline");
        listForBuilder.add("--");
        listForBuilder.add("./"+file.ConstructPathWithoutProjectid());

        return  execCommande(project.getIdProject(), listForBuilder);
    }

    /**
     * Prépare la commande pour le "git checkout" d'un seul fichier et l'execute par l'intermédiaire de "execCommande"
     * @param project Le projet
     * @param file Le fichier sur lequel effectuer le checkout
     * @param idCommit L'id du commit sur lequel revenir
     * @return La sortie de la commande "git checkout"
     * @see FileManager#execCommande(int, List)
     */
    public String gitCheckoutOneFile(Project project, File file, String idCommit){
        List<String> listForBuilder=new ArrayList<String>();
        listForBuilder.add("git");
        listForBuilder.add("checkout");
        listForBuilder.add(idCommit);
        listForBuilder.add("./"+file.ConstructPathWithoutProjectid());

        return  execCommande(project.getIdProject(), listForBuilder);
    }

    /**
     * Chaque fonction ayant besoin d'executer une commande sur le serveur et seulement dans le répertoire du projet peut utiliser cette méthode
     * @param projectid L'identifian du projet
     * @param listForBuilder La composition de la commande exemple: ["git","log"] donne "git log"
     * @return Le retour de la commande executé
     */
    public String execCommande(int projectid, List<String> listForBuilder){
        String s;
        String output="";

        try {
            ProcessBuilder builder = new ProcessBuilder(listForBuilder);
            builder.directory(new java.io.File("./src/main/webapp/static_website/projects/"+projectid));
            builder.redirectErrorStream(true);
            Process p =  builder.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null) {
                output+=s+"\n";
            }
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            logger.error(e);
        }
        return  output;
    }

}
