package com.ag.PatchForge.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

@Service
public class ZipService {

    public void zipFolder(Path sourceFolder, Path destinationZip) {
        String rootFolderName = sourceFolder.getFileName().toString();

        try (OutputStream fos = Files.newOutputStream(destinationZip);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            try (Stream<Path> walk = Files.walk(sourceFolder)) {
                walk.filter(Files::isRegularFile)
                    .forEach(file -> addFileToZip(zos, sourceFolder, rootFolderName, file));
            }

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to create zip: " + destinationZip, e);
        }
    }

    private void addFileToZip(ZipOutputStream zos, Path sourceFolder, String rootFolderName, Path file) {
        try {
           
            Path relative = sourceFolder.relativize(file);
            String entryName = rootFolderName + "/" + relative.toString().replace('\\', '/');

            zos.putNextEntry(new ZipEntry(entryName));
            try (InputStream in = Files.newInputStream(file)) {
                in.transferTo(zos);
            }
            zos.closeEntry();

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to add file to zip: " + file, e);
        }
    }
}