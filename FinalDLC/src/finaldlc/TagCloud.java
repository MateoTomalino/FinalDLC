package finaldlc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class TagCloud {

    private static TagCloud self;
    private HashMap<String, Integer> tagCloudMap;

    private TagCloud() {
        tagCloudMap = new HashMap<>();
        File file = new File("tagCloud.cloud");
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            if (file.exists()) {
                tagCloudMap = (HashMap<String, Integer>) ois.readObject();
            }
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println("Error en TagCloud: " + ex.toString());
        }
    }

    public static TagCloud getInstance() {
        if (self == null) {
            self = new TagCloud();
        }
        return self;
    }

    public void guardar() {
        try {
            File file = new File("tagCloud.cloud");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            if (!file.exists()) {
                file.createNewFile();
            }
            oos.writeObject(tagCloudMap);
        } catch (IOException ex) {
            System.out.println("Error en TagCloud guardar(): " + ex.toString());
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<div id=\"whatever\">");
        for (Map.Entry<String, Integer> temp : tagCloudMap.entrySet()) {
            builder.append("<a href='http://localhost:8080/oven/BuscadorController?consulta=" + temp.getKey() + "' rel='" + temp.getValue().intValue() + "'>" + temp.getKey() + "</a>");
        }
        builder.append("</div>");
        return builder.toString();
    }

    public void agregar(String termino) {
        Integer valor = tagCloudMap.get(termino);
        if (valor == null) {
            valor = 0;
        }
        valor++;
        tagCloudMap.put(termino, valor);
    }
}
