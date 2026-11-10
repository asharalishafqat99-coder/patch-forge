package com.ag.PatchForde;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.ag.PatchForge.dto.ResolvedClass;
import com.ag.PatchForge.exception.ClassNotFoundInWarException;
import com.ag.PatchForge.service.WarExtractionService;

class WarExtractionServiceTest {

    private final WarExtractionService service = new WarExtractionService();

    @TempDir
    Path tempDir;

    private Path fakeWar;
    private static final byte[] FAKE_CLASS_BYTES = "not real bytecode, just a test payload".getBytes(StandardCharsets.UTF_8);

    @BeforeEach
    void buildFakeWar() throws IOException {
        fakeWar = tempDir.resolve("fake.war");

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(fakeWar))) {
            zos.putNextEntry(new ZipEntry("WEB-INF/classes/com/ag/config/SomeClass.class"));
            zos.write(FAKE_CLASS_BYTES);
            zos.closeEntry();
        }
    }

    @Test
    void extractsExistingClassIntoFlatDottedFolder() throws IOException {
        Path patchRoot = tempDir.resolve("patch");
        Files.createDirectories(patchRoot);

        ResolvedClass resolvedClass = new ResolvedClass("com.ag.config", "SomeClass",
                "src\\main\\ag-mw-upd\\com\\ag\\config\\SomeClass.java");

        service.extractInto(fakeWar, resolvedClass, patchRoot);

        Path expectedFile = patchRoot.resolve("com.ag.config").resolve("SomeClass.class");
        assertTrue(Files.exists(expectedFile), "Expected extracted file to exist at " + expectedFile);
        assertArrayEquals(FAKE_CLASS_BYTES, Files.readAllBytes(expectedFile));
    }

    @Test
    void throwsWhenClassNotInWar() {
        Path patchRoot = tempDir.resolve("patch");

        ResolvedClass missingClass = new ResolvedClass("com.ag.missing", "NoSuchClass",
                "src\\main\\ag-mw-upd\\com\\ag\\missing\\NoSuchClass.java");

        assertThrows(ClassNotFoundInWarException.class, () ->
                service.extractInto(fakeWar, missingClass, patchRoot));
    }
}