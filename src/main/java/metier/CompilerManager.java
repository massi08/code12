package metier;

import modele.Project;
import modele.User;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class CompilerManager : classe regroupant tout les fonctions pour compiler un projet (Java, C++, C).
 */
public class CompilerManager {
    private Logger logger = Logger.getLogger(CompilerManager.class);
    private DisplayFlux outputFlux;
    private DisplayFlux errorsFlux;
    private FileManager fileManager;
    private File folder;
    private String nameProject;
    private Integer idLanguage;
    private String javaMainFile;
    private ArrayList<String> filesList = new ArrayList<>();
    private ArrayList<String> newFilesList = new ArrayList<>();

    private static final Pattern javaMainFilePattern = Pattern.compile(".*static void main.*");
    private static String defaulErrorLog = "<h7>Sortie d'erreurs :</h7>" + "<p>Aucunes informations à afficher.</p>";
    private static String makefile = "Makefile";

    /**
     * Choisit quelle fonction de compilation à appeller en fonction du langage, projet en entrée
     *
     * @param fileManager  : manager
     * @param folder  : dossier contenant les sources
     * @param user    : le user connecté
     * @param project : le projet
     * @return output : les logs de compilation
     */
    public String interpreter(FileManager fileManager, File folder, User user, Project project) throws InterruptedException {
        this.fileManager = fileManager;
        this.folder = folder;
        this.nameProject = project.getName();
        this.idLanguage = project.getLanguage().getIdLanguage();

        ArrayList<String> logs = new ArrayList<>();
        StringBuilder output = new StringBuilder();

        if (idLanguage == 1) {
            logs = (ArrayList<String>) compilerJava();

        } else if (idLanguage == 2 || idLanguage == 3) {
            logs = (ArrayList<String>) compilerC();
        }

        for (int i = 0; i < newFilesList.size(); i++) {
            String path = "/" + newFilesList.get(i).substring(0, newFilesList.get(i).lastIndexOf('/') + 1);
            String nameFile = newFilesList.get(i).substring(newFilesList.get(i).lastIndexOf('/') + 1, newFilesList.get(i).length());
            List<modele.File> projectFiles = fileManager.getFileByProject(project);
            boolean contains = false;
            for (int j = 0; j < projectFiles.size(); j++) {
                if (projectFiles.get(j).getNameFile().equals(nameFile))
                    contains = true;
            }
            if (!contains)
                fileManager.createFile(user, project, path, nameFile);
        }

        for (int i = 0; i < logs.size(); i++)
            output.append(logs.get(i));
        return output.toString();
    }

    /**
     * Execute les commandes pour compiler le projet Java
     *
     * @return output : les logs de compilation
     */
    public List<String> compilerJava() throws InterruptedException {
        StringBuilder files = new StringBuilder();
        ArrayList<String> logs = new ArrayList<>();
        ArrayList<String> listCommand = new ArrayList<>();

        // on récupère les fichiers sources ainsi que le main
        File currentFolder = this.folder;
        getFiles(currentFolder, "java");
        for (int i = 0; i < this.filesList.size(); i++)
            files.append(this.filesList.get(i) + " ");

        // On crée les commandes de compilation
        listCommand.add(0, "javac");
        listCommand.add(1, files.toString());
        listCommand.add(2, this.javaMainFile);

        // On lance la commande de compilation
        logs.addAll(launchCommand(listCommand.get(0) + " " + listCommand.get(1).substring(0, listCommand.get(1).length() - 1)));

        // Si l'output est vide, la compilation a fonctionnée, on crée le manifest puis le jar
        if (logs.get(logs.size() - 2).equals(defaulErrorLog)) {
            try {
                String manifestContent = "Manifest-Version: 1.0\nClass-Path: .\nMain-Class: " + listCommand.get(2) + "\n";
                Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.folder + "/Manifest.mf")));
                writer.write(manifestContent);
                writer.close();
            } catch (IOException e) {
                logger.error(e);
            }
            logs.addAll(launchCommand("jar -cvfm exec.jar Manifest.mf " + listCommand.get(1).substring(0, listCommand.get(1).length() - 1)));
        }
        return logs;
    }

    /**
     * Execute les commandes pour compiler le projet C ou C++
     *
     * @return output : les logs de compilation
     */
    public List<String> compilerC() throws InterruptedException {
        StringBuilder files = new StringBuilder();
        ArrayList<String> logs = new ArrayList<>();
        ArrayList<String> listCommand = new ArrayList<>();

        // on récupère les fichiers sources en passant le format de l'extension (soit cpp ou c)
        String tmpString = this.idLanguage == 2 ? "cpp" : "c";
        File currentFolder = this.folder;
        getFiles(currentFolder, tmpString);
        for (int i = 0; i < this.filesList.size(); i++)
            files.append(this.filesList.get(i) + " ");

        // Si on a trouvé un makefile, on l'éxécute. Sinon on crée les commandes de compilation
        if (files.toString().equals(makefile)) {
            listCommand.add("make clean");
            logs.addAll(launchCommand(listCommand.get(0)));
            listCommand.add("make");
            logs.addAll(launchCommand(listCommand.get(0)));
        } else {
            tmpString = this.idLanguage == 2 ? "g++" : "gcc";
            listCommand.add(tmpString);
            listCommand.add(files.toString().trim());
            listCommand.add("-o");
            listCommand.add("exec");
            logs.addAll(launchCommand(listCommand.get(0) + " " + listCommand.get(1) + " " + listCommand.get(2) + " " + listCommand.get(3)));
        }
        return logs;
    }

    /**
     * Permet de lancer une commande dans un processus à part.
     *
     * @param command : la commande a lancé
     * @return logs : les logs de compilation
     */
    private ArrayList<String> launchCommand(String command) throws InterruptedException {
        try {
            Process process = Runtime.getRuntime().exec(command, null, this.folder);

            outputFlux = new DisplayFlux(process.getInputStream(), "Sortie standard :");
            errorsFlux = new DisplayFlux(process.getErrorStream(), "Sortie d'erreurs :");
            new Thread(outputFlux).start();
            new Thread(errorsFlux).start();
            process.waitFor();

        } catch (IOException e) {
            logger.error(e);
        }

        File currentFolder = this.folder;
        getNewFiles(currentFolder);

        ArrayList<String> logs = new ArrayList<>();
        logs.add("<h6> * Commande: " + command + "</h6>");
        logs.add(errorsFlux.getLog());
        logs.add(outputFlux.getLog());
        return logs;
    }

    /**
     * Parcourt récursivement et stocke les fichiers sources à compiler et détecte la présence de makefile en c/c++.
     *
     * @param format : extension des fichiers à chercher
     * @return list des fichiers sources
     */
    private void getFiles(File currentFolder, String format) {
        if (this.filesList.contains("makefile")) {
            return;
        }

        File[] listOfFiles = currentFolder.listFiles();
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                currentFolder = file;
                getFiles(currentFolder, format);
            } else {
                if (file.getName().equalsIgnoreCase(makefile)) {
                    this.filesList.clear();
                    this.filesList.add(makefile);
                    return;
                }
                String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1, file.getName().length());
                if (extension.equals(format)) {
                    String nameFile = file.getParentFile().getName().equals(this.nameProject) ? (file.getName() + " ") : (file.getParentFile().getName() + "/" + file.getName() + " ");
                    if (!this.filesList.contains(nameFile))
                        this.filesList.add(nameFile);
                    findJavaMain(file, this.folder.getAbsolutePath() + "/" + nameFile.trim(), extension);
                }
            }
        }
    }

    /**
     * Parcourt récursivement et stocke les nouveaux fichiers résultant de la compilation.
     *
     * @return list des fichiers sources
     */
    private void getNewFiles(File currentFolder) {
        File[] listOfFiles = currentFolder.listFiles();
        for (File file : listOfFiles) {
            if (file.isDirectory()) {
                currentFolder = file;
                getNewFiles(currentFolder);
            } else {
                File parent = file;
                StringBuilder builder = new StringBuilder();
                while (!parent.getName().equals(this.nameProject)) {
                    builder.insert(0, "/" + parent.getName());
                    parent = parent.getParentFile();
                }
                String name = builder.toString().substring(1, builder.toString().length());
                boolean contains = false;
                for (int i = 0; i < this.filesList.size(); i++) {
                    if (this.filesList.get(i).trim().equals(name))
                        contains = true;
                }
                if (!contains) {
                    this.filesList.add(name);
                    this.newFilesList.add(name);
                }
            }
        }
    }

    /**
     * Parcourt le fichier pour détecter la présence (ou non) d'un main en java.
     *
     * @return true ou false suivant si un main est présent
     * @paramf file : fichier
     */
    private void findJavaMain(File file, String filePath, String fileExtension) {
        if (!"java".equals(fileExtension))
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (javaMainFilePattern.matcher(line.trim()).matches()) {
                    this.javaMainFile = file.getParentFile().getName().equals(this.nameProject) ? file.getName().substring(0, file.getName().lastIndexOf('.')) :
                            file.getParentFile().getName() + "." + file.getName().substring(0, file.getName().lastIndexOf('.'));
                    return;
                }
            }
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * Class DisplayFlux : Classe interne permettant de collecter le flux de données résultant de la compilation.
     */
    class DisplayFlux implements Runnable {

        private final InputStream inputStream;
        private String finalOutput;
        private String nameOutput;
        private StringBuilder log;

        DisplayFlux(InputStream inputStream, String nameOutput) {
            this.inputStream = inputStream;
            this.finalOutput = new String();
            this.nameOutput = nameOutput;
            this.log = new StringBuilder();
        }

        private BufferedReader getBufferedReader(InputStream is) {
            return new BufferedReader(new InputStreamReader(is));
        }

        @Override
        public void run() {
            BufferedReader br = getBufferedReader(inputStream);
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    log.append("<p>" + line + "</p>");
                }
            } catch (IOException e) {
                logger.error(e);
            }
            this.finalOutput = "".equals(log.toString()) ? ("<h7>" + nameOutput + "</h7>" + "<p>Aucunes informations à afficher.</p>")
                    : ("<h7>" + nameOutput + "</h7>" + log);
        }

        public String getLog() {
            return this.finalOutput;
        }
    }
}