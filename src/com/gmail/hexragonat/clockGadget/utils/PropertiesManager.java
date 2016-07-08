package com.gmail.hexragonat.clockGadget.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class PropertiesManager
{
    private Properties prop;
    private Properties defaultProp;
    public File file;
    public final String path;
    public final String fileName;
    public final Object anchor;

    public PropertiesManager(Object anchor, String path, String fileName)
    {
        this.anchor = anchor;
        this.path = path;
        this.fileName = fileName;
    }

    public void load()
    {
        load(this.file, anchor.getClass().getResourceAsStream(this.path));
    }

    public void load(File file, InputStream is)
    {
        try
        {
            System.out.println(is);
            if (file == null)
            {
                file = new File(fileName);

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

    public void setDefault(InputStream is)
    {
        try
        {
            defaultProp = new Properties();
            defaultProp.load(is);
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

        if (s == null)
        {
            if (defaultProp != null)
            {
                String def = defaultProp.getProperty(key);
                if (def != null)
                {
                    return def;
                }
            }
            return null;
        }

        if (obj.length > 0) s = String.format(s, obj);
        return s;
    }

    public String getDefault(String key)
    {
        return defaultProp.getProperty(key);
    }

    public void set(String key, String value)
    {
        prop.setProperty(key, value);
    }

    public void save()
    {
        if (file == null)
        {
            System.out.println("No file found to save to!");
        }
        OutputStream out;
        try
        {
            out = new FileOutputStream(file);
            prop.store(out, null);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
