package com.jacky.tool.apk.tasks;

import com.jacky.tool.apk.results.res.AsrcTaskResult;
import com.jacky.tool.task.ITask;
import com.jacky.tool.task.TaskResult;
import com.jacky.tool.util.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import brut.androlib.AndrolibException;
import brut.androlib.res.data.ResPackage;
import brut.androlib.res.data.ResResSpec;
import brut.androlib.res.data.ResResource;
import brut.androlib.res.data.ResTable;
import brut.androlib.res.data.ResValuesFile;
import brut.androlib.res.data.value.ResFileValue;
import brut.androlib.res.decoder.ARSCDecoder;
import brut.androlib.res.decoder.AXmlResourceParser;
import brut.androlib.res.decoder.ResAttrDecoder;
import brut.androlib.res.decoder.XmlPullStreamDecoder;
import brut.androlib.res.util.ExtMXSerializer;

/**
 * Created by Jacky on 2020/5/28
 */
public class AsrcTask implements ITask {
    private final File asrcFile;

    public AsrcTask(File asrcFile) {
        this.asrcFile = asrcFile;
    }

    @Override
    public TaskResult call() throws Exception {
        final AsrcTaskResult asrcTaskResult = new AsrcTaskResult(this);
        ResTable resTable = new ResTable();
        decodeArscFile(resTable);
        return asrcTaskResult;
    }

    private void decodeArscFile(ResTable resTable) throws IOException, AndrolibException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(asrcFile));
        try {
            try {
                ResPackage[] resPackages = ARSCDecoder
                    .decode(inputStream, false, true, resTable).getPackages();

                final AXmlResourceParser resourceParser = new AXmlResourceParser();
                resourceParser.setAttrDecoder(new ResAttrDecoder());
                final ExtMXSerializer extMXSerializer = new ExtMXSerializer();

                final String inputDir = "/Users/yangjianfei/Downloads/test/apk-checker/5.4.3.16";
                final File resDir = new File(inputDir, "res");

                final XmlPullStreamDecoder xmlPullStreamDecoder =
                    new XmlPullStreamDecoder(resourceParser, extMXSerializer);
                for (ResPackage resPackage : resPackages) {
                    final Set<ResResource> resResources = resPackage.listFiles();
                    final List<ResResSpec> resResSpecs = resPackage.listResSpecs();
                    final Collection<ResValuesFile> resValuesFiles = resPackage.listValuesFiles();

                    resourceParser.getAttrDecoder().setCurrentPackage(resPackage);

                    for (ResResource resResource : resResources) {
                        ResFileValue value = (ResFileValue) resResource.getValue();
                        final String strippedPath = value.getStrippedPath();
                        if (!strippedPath.endsWith(".xml")) {
                            continue;
                        }

                        final File file = new File(resDir, strippedPath);
                        if (file.exists()) {
                            Util.i("file[%s]", file.getAbsolutePath());
                        }

//                        final File out = new File(inputDir, "out.xml");
//                        xmlPullStreamDecoder.decode(new FileInputStream(file), new FileOutputStream(out));
//                        break;
                    }
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw e;
        }
    }


    @Override
    public void init() {
        if (!asrcFile.exists()) {
            throw new IllegalStateException("file not found");
        }

        if (asrcFile.length() == 0 || !asrcFile.canRead() || !asrcFile.isFile()) {
            throw new IllegalStateException("arsc file is not readable or length is 0");
        }
    }
}
