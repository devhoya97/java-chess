package chess.db.translator;

import chess.domain.position.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FileTranslatorTest {

    @DisplayName("file에 맞는 dbView를 찾는다.")
    @Test
    void translateFileToDbView() {
        String dbView = FileTranslator.translate(File.A);

        assertThat(dbView).isEqualTo("a");
    }

    @DisplayName("dbView에 맞는 file을 찾는다.")
    @Test
    void translateDbViewToFile() {
        File file = FileTranslator.translate("a");

        assertThat(file).isEqualTo(File.A);
    }
}