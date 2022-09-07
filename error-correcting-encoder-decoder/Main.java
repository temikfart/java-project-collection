package correcter;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        System.out.print("Write a mode: ");
        String command = new Scanner(System.in).nextLine();
        if ("encode".equals(command)) writeToFile("encoded.txt", encode(readFromFile("send.txt")));
        if ("send".equals(command)) writeToFile("received.txt", send(readFromFile("encoded.txt")));
        if ("decode".equals(command)) writeToFile("decoded.txt", decode(readFromFile("received.txt")));
    }

    public static byte[] encode(byte[] byteData) {
        var encodedData = new ArrayList<Byte>();
        int encoded = 0;
        for (var b : byteData) {
            int offset = 0;
            int bitPosition = 0;
            for (int i = 128; i > 0; i >>>= 1) {
                if (bitPosition == 0) {
                    offset = 3;
                    bitPosition = 8 - offset;
                }

                int bit = (b & i) > 0 ? 1 : 0;
                encoded |= bit << bitPosition;

                offset++;
                while (offset % 2 == 0 && offset != 6 && offset != 8)
                    offset++;
                bitPosition = 8 - offset;

                if (bitPosition == 0) {
                    encoded |= ((encoded >> 5 & 0x1) ^ (encoded >> 3 & 0x1) ^ (encoded >> 1 & 0x1)) << 7;
                    encoded |= ((encoded >> 5 & 0x1) ^ (encoded >> 2 & 0x1) ^ (encoded >> 1 & 0x1)) << 6;
                    encoded |= ((encoded >> 3 & 0x1) ^ (encoded >> 2 & 0x1) ^ (encoded >> 1 & 0x1)) << 4;
                    encoded &= 0xFE;
                    encodedData.add((byte) encoded);
                    encoded = 0;
                }
            }
        }
        for (byte b : encodedData)
            System.out.printf("%X ", b);
        return convertBytes(encodedData);
    }

    public static byte[] decode(byte[] byteData) {
        var decodedData = new ArrayList<Byte>();
        int n = 0;
        while (n < byteData.length) {
            byte decoded = 0;

            byte x = correctErrors(byteData[n++]);
            byte y = correctErrors(byteData[n++]);

            decoded |= (x >> 5 & 0x1) << 7;
            decoded |= (x >> 3 & 0x1) << 6;
            decoded |= (x >> 2 & 0x1) << 5;
            decoded |= (x >> 1 & 0x1) << 4;

            decoded |= (y >> 5 & 0x1) << 3;
            decoded |= (y >> 3 & 0x1) << 2;
            decoded |= (y >> 2 & 0x1) << 1;
            decoded |= (y >> 1 & 0x1);

            decodedData.add(decoded);
        }
        return convertBytes(decodedData);
    }

    private static byte correctErrors(byte b) {
        byte p8Bit = (byte) (b & 0x1);
        if (p8Bit != 0)
            return (byte)(b & 0xFE);

        int pos = 0;

        byte p1Bit = (byte) ((b >> 5 & 0x1) ^ (b >> 3 & 0x1) ^ (b >> 1 & 0x1));
        if (p1Bit != (byte) (b >> 7 & 0x1))
            pos += 1;

        byte p2Bit = (byte) ((b >> 5 & 0x1) ^ (b >> 2 & 0x1) ^ (b >> 1 & 0x1));
        if (p2Bit != (byte) (b >> 6 & 0x1))
            pos += 2;

        byte p4Bit = (byte) ((b >> 3 & 0x1) ^ (b >> 2 & 0x1) ^ (b >> 1 & 0x1));
        if (p4Bit != (byte) (b >> 4 & 0x1))
            pos += 4;

        int offset = 8 - pos;
        byte corBit = (byte) ((~(b >> offset)) & 0x1);
        if (corBit == 1)
            return b |= 1 << offset;
        else
            return b &= ~(1 << offset);
    }

    public static byte[] send(byte[] byteData) {
        for (int i = 0; i < byteData.length; i++) {
            byteData[i] = (byte) (byteData[i] ^ (1 << new Random().nextInt(7)));
        }
        return byteData;
    }

    public static byte[] convertBytes(List<Byte> bytes) {
        byte[] unboxed = new byte[bytes.size()];
        IntStream.range(0, bytes.size()).forEach(i -> unboxed[i] = bytes.get(i));
        return unboxed;
    }

    private static byte[] readFromFile(String filename) {
        try (var in = new FileInputStream(filename)) {
            return in.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private static void writeToFile(String filename, byte[] data) {
        try (var out = new FileOutputStream(filename)) {
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
