package com.gmail.hexragonat.clockGadget.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class PropertiesManager
{
    private Properties prop;
    private File file;
    public final String fileName;

    public PropertiesManager(String s)
    {
        fileName = s;
    }

    public void load()
    {
        load(this.file, this.getClass().getResourceAsStream(fileName));
    }

    public void load(File file, InputStream is)
    {
        try
        {
            if (file == null)
            {
                file = new File("data"+File.separator+fileName);

                if (!file.exists())
                {
                    if (file.mkdirs())
                    {
                        System.out.println(String.format("Making directory for file '%s'.", fileName));
                    }
                    Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    System.out.println(String.format("Copying file '%s' from jar to disk.", fileName));
                }
                this.file = file;
            }

            InputStream is2 = new FileInputStream(this.file);
            load(is2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void load(InputStream is)
    {
        try
        {
            prop = new Properties();
            prop.load(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String get(String key, Object... obj)
    {
        if (prop == null) return "PropertyManager error!";
        String s = prop.getProperty(key);
        if (obj.length > 0) s = String.format(s, obj);
        return s;
    }
}
