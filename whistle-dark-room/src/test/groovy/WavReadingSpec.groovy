import spock.lang.Ignore
import spock.lang.Specification

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem


class WavReadingSpec extends Specification {

    def "read first wav file and expect output as string"() {
        expect:
        readFirstDataByte(0) == 0
        readFirstDataByte(1) == 0
    }

    @Ignore
    def "read bytes until not a zero"() {
        expect:
        readFirstNotZero() == 0
    }

    def "read as audio stream"() {
        expect:
        readFromAudioStream(0) == 0
        readFromAudioStream(1) == 0
        readFromAudioStream(2) == 0
        readFromAudioStream(3) == 0
        readFromAudioStream(4) == 0
    }

    def "read first not zero from audio stream"() {
        expect:
        readFromAudioStreamFirstNotZero()  == 2
    }

    def readFromAudioStream(index) {
        readFromWav { InputStream stream ->

            def format = new AudioFormat(44100f, 8, 1, true, false)
            AudioFormat.Encoding.PCM_SIGNED
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream)
            AudioInputStream wav = AudioSystem.getAudioInputStream(format, audioStream)
            wav.read(new byte[50], 0, 44)
            wav.skip(index)
            wav.read().byteValue()

        }
    }

    def readFromAudioStreamFirstNotZero() {
        readFromWav { InputStream stream ->

            def format = new AudioFormat(44100f, 8, 1, true, false)
            AudioFormat.Encoding.PCM_SIGNED
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream)
            AudioInputStream wav = AudioSystem.getAudioInputStream(format, audioStream)
            wav.read(new byte[50], 0, 44)
            def readByte = 0
            while (readByte == 0) {
                readByte = wav.read().byteValue()    // 0 = 128 not possible if no 0...
            }
            readByte

        }
    }

    Integer readFirstNotZero() {
        readFromWav({ InputStream stream ->
            stream.read(new byte[50], 0, 44)
            def readByte = 0
            while (readByte == 0) {
                readByte = stream.read() + 128     // 0 = 128 not possible if no 0...
            }
            return readByte

        })
    }
// -128 ... 0 ... 128  //+128     // 0 is ignored / not possible
    //8    7   6   5   4   3   2   1  0
    //256  128 64  32  16  8   4   2  1

    private int readFirstDataByte(int index) {
        readFromWav({ InputStream stream ->

            def buff = new byte[1000];
            stream.read(buff, 0, 44)
            stream.read(buff, 0, index + 1)
            return buff[index] + 128

        })

    }

    def readFromWav(closure) {
        return WavReadingSpec.class.getResourceAsStream("/PROBLEMSET/input/P/1.wav").withStream(closure)
    }


    def whatever() {
        int totalFramesRead = 0;
        File fileIn = new File(somePathName);
        // somePathName is a pre-existing string whose value was
        // based on a user selection.
        try {
            AudioInputStream audioInputStream =
                AudioSystem.getAudioInputStream(fileIn);
            int bytesPerFrame =
                audioInputStream.getFormat().getFrameSize();
            if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
                // some audio formats may have unspecified frame size
                // in that case we may read any amount of bytes
                bytesPerFrame = 1;
            }
            // Set an arbitrary buffer size of 1024 frames.
            int numBytes = 1024 * bytesPerFrame;
            byte[] audioBytes = new byte[numBytes];
            try {
                int numBytesRead = 0;
                int numFramesRead = 0;
                // Try to read numBytes bytes from the file.
                while ((numBytesRead =
                    audioInputStream.read(audioBytes)) != -1) {
                    // Calculate the number of frames actually read.
                    numFramesRead = numBytesRead / bytesPerFrame;
                    totalFramesRead += numFramesRead;
                    // Here, do something useful with the audio data that's
                    // now in the audioBytes array...
                }
            } catch (Exception ex) {
                // Handle the error...
            }
        } catch (Exception e) {
            // Handle the error...
        }

    }

}