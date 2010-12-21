/*
 */

package jgf.net;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Provide a set of utility operations over ByteBuffers.
 * 
 * @author Mendonça, Vin?cius Godoy de
 */
public class ByteBufferWorker
{

    public static short getUnsignedByte(ByteBuffer bb)
    {
        return ((short) (bb.get() & 0xff));
    }

    public static void putUnsignedByte(ByteBuffer bb, int value)
    {
        bb.put((byte) (value & 0xff));
    }

    public static short getUnsignedByte(ByteBuffer bb, int position)
    {
        return ((short) (bb.get(position) & (short) 0xff));
    }

    public static void putUnsignedByte(ByteBuffer bb, int position, int value)
    {
        bb.put(position, (byte) (value & 0xff));
    }

    public static int getUnsignedShort(ByteBuffer bb)
    {
        return (bb.getShort() & 0xffff);
    }

    public static void putUnsignedShort(ByteBuffer bb, int value)
    {
        bb.putShort((short) (value & 0xffff));
    }

    public static int getUnsignedShort(ByteBuffer bb, int position)
    {
        return (bb.getShort(position) & 0xffff);
    }

    public static void putUnsignedShort(ByteBuffer bb, int position, int value)
    {
        bb.putShort(position, (short) (value & 0xffff));
    }

    public static long getUnsignedInt(ByteBuffer bb)
    {
        return bb.getInt() & 0xffffffffL;
    }

    public static void putUnsignedInt(ByteBuffer bb, long value)
    {
        bb.putInt((int) (value & 0xffffffffL));
    }

    public static long getUnsignedInt(ByteBuffer bb, int position)
    {
        return bb.getInt(position) & 0xffffffffL;
    }

    public static void putUnsignedInt(ByteBuffer bb, int position, long value)
    {
        bb.putInt(position, (int) (value & 0xffffffffL));
    }

    /**
     * Read all bytebuffer data and returns it as an string. This action does
     * not change the bytebuffer pointers. To take only the remaining bytes, use
     * the {@link #getAsString(ByteBuffer)} method.
     * <p>
     * Some methods may avoid reading the entire buffer in memory if only a
     * CharSequence is needed. In this case, use the
     * {@link #toCharSequence(ByteBuffer)} method instead.
     * 
     * @param data The bytebuffer to read
     * @return The same data, as an string.
     * @see #toCharSequence(ByteBuffer)
     * @see #getAsString(ByteBuffer)
     * @see #getCString(ByteBuffer)
     */
    public static String toString(ByteBuffer data)
    {
        int pos = data.position();
        data.position(0);
        String text = asString(data);
        data.position(pos);

        return text;
    }

    /**
     * This method wraps the entire ByteBuffer into a CharSequence, considering
     * each byte as an individual character(ascii). Using wrapper methods
     * <b>will change</b> byte buffer position.
     * <p>
     * Changes to the buffer will affect the char sequence.
     * 
     * @param buffer The ByteBuffer to be wrapped.
     * @return A CharSequence, with the buffer wrapped in.
     * @see #toString(ByteBuffer)
     * @see #getAsString(ByteBuffer)
     * @see #getCString(ByteBuffer)
     */
    public static CharSequence toCharSequence(final ByteBuffer buffer)
    {
        return new CharSequence()
        {
            public char charAt(int index)
            {
                return (char) buffer.get(index);
            }

            public int length()
            {
                return buffer.limit();
            }

            public CharSequence subSequence(int start, int end)
            {
                if (start == end)
                    return "";

                buffer.position(start);
                byte[] seq = new byte[end - start];
                buffer.get(seq);

                return new String(seq);
            }
        };
    }

    /**
     * Read all remaining bytebuffer data and returns it as an string. This
     * action does not change the bytebuffer pointers.
     * <p>
     * To read bytes until a \0 is reachead (like C Strings), use the
     * {@link #getCString(ByteBuffer)} method instead.
     * 
     * @param data The bytebuffer to read
     * @return The same data, as an string.
     * @see #getCString(ByteBuffer)
     * @see #toString(ByteBuffer)
     * @see #toCharSequence(ByteBuffer)
     */
    public static String asString(ByteBuffer data)
    {
        if (data == null || data.remaining() == 0)
            return "";

        int pos = data.position();
        int lim = data.limit();

        byte dataBuffer[] = new byte[data.remaining()];
        data.get(dataBuffer);

        data.position(pos);
        data.limit(lim);

        return new String(dataBuffer, 0, dataBuffer.length);
    }

    /**
     * Create a new ByteBuffer with the size and a copy of the remaining data
     * from the given source ByteBuffer. The source position will not be
     * changed.
     * 
     * @param source The bytebuffer that will have the remaining data extracted.
     * @return The new ByteBuffer, with the remaining data from the source.
     */
    public static ByteBuffer createBufferFromData(ByteBuffer source)
    {
        ByteBuffer dest = ByteBuffer.allocate(source.remaining()).order(
                source.order());
        int oldPos = source.position();
        dest.put(source).flip();
        source.position(oldPos);
        return dest;
    }

    /**
     * Read a C like string. The read will start in the position and will finish
     * in the null char. <br>
     * After the conversion, the position will be moved after the null char. If
     * there's no null char, the entire ByteBuffer will be read.
     * 
     * @param data The data to read the string
     * @return The read string
     */
    public static String getCString(ByteBuffer data)
    {
        byte dataBuffer[] = new byte[data.remaining()];
        int len = 0;
        while (data.hasRemaining())
        {
            byte character = data.get();
            if (character == 0)
                break;
            dataBuffer[len] = character;
            len++;
        }
        return new String(dataBuffer, 0, len);
    }
    
    public static String getString(ByteBuffer data)
    {
        int len = getUnsignedByte(data);
        
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(data.getChar());
        return sb.toString();    
    }
    
    public static void putString(ByteBuffer bb, String value)
    {
        if (value.length() > 255)
            value = value.substring(0, 255);
        
        putUnsignedByte(bb, value.length());
        for (char ch : value.toCharArray())
            bb.putChar(ch);
    }

    private static String toHex(int value, int size)
    {
        String hex = Integer.toHexString(value).toUpperCase();

        while (hex.length() < size)
            hex = "0" + hex;

        return hex;
    }

    private static char shortToChar(short character, char nonPrintable)
    {
        return character < 0x20 ? nonPrintable : (char) character;
    }

    /**
     * Mount a String containing all bytebuffer data in a dump format. The above
     * dump format use 16 bytes per line and '.' as a non-printable.:
     * 
     * <pre>
     *      [0000] 54 68 69 73 20 69 73 20 61 20 73 61 6D 70 6C 65 |This is a sample
     *      [0010] 20 64 75 6D 70 13 66 6F 72 6D 61 74 21          | dump.format!     
     * </pre>
     * 
     * The <code>nonPrintable</code> replaces some non-printable characters,
     * such as line feed. This can be seen between the words "dump format" in
     * the sample.
     * 
     * @param data The bytebuffer data to dump. Using this method will not
     *        affect the ByteBuffer position.
     * @param bytesPerLine Number of bytes to print in one line.
     * @param nonPrintable Character that will replace non-printable chars.
     * @return The dump string.
     * @see #asDumpString(ByteBuffer)
     */
    public static String asDumpString(ByteBuffer data, int bytesPerLine,
            char nonPrintable)
    {
        if (data == null || !data.hasRemaining())
            return "";

        int oldPosition = data.position();

        StringBuilder result = new StringBuilder();

        int line = 0;

        while (data.hasRemaining())
        {
            StringBuilder hexPart = new StringBuilder(3 * bytesPerLine);
            StringBuilder asciiPart = new StringBuilder(bytesPerLine);
            int bytesWritten = 0;
            result.append("[" + toHex(line * bytesPerLine, 4) + "] ");

            while (data.hasRemaining() && bytesWritten < bytesPerLine)
            {
                short ch = getUnsignedByte(data);
                hexPart.append(toHex(ch, 2) + " ");
                asciiPart.append(shortToChar(ch, nonPrintable));
                bytesWritten++;
            }

            while (bytesWritten++ < bytesPerLine)
                hexPart.append("   ");

            result.append(hexPart).append("|").append(asciiPart).append("\n");
            line++;
        }

        data.position(oldPosition);

        return result.toString();
    }

    /**
     * Mount a String containing all bytebuffer data in a dump format. The dump
     * format is just like the above:
     * 
     * <pre>
     *      [0000] 54 68 69 73 20 69 73 20 61 20 73 61 6D 70 6C 65 |This is a sample
     *      [0010] 20 64 75 6D 70 13 66 6F 72 6D 61 74 21          | dump.format!     
     * </pre>
     * 
     * This method uses 16 bytes per line and '·' to replace non-printable
     * characters.
     * 
     * @param data The bytebuffer data to dump. Using this method will not
     *        affect the ByteBuffer position.
     * @return The dump string.
     * @see #getDumpString(ByteBuffer, int, char)
     */
    public static String asDumpString(ByteBuffer data)
    {
        return asDumpString(data, 16, '.');
    }

    /**
     * Returns a flipped bytebuffer filled with the given string. The bytes of
     * the string will be obtained by calling getBytes().
     * 
     * @param text The string to fill the buffer.
     * @return The bytebuffer.
     */
    public static ByteBuffer createFromString(String text)
    {
        byte[] buf = text.getBytes();
        return (ByteBuffer) ByteBuffer.allocate(buf.length).put(buf).flip();
    }

    /**
     * Returns a flipped bytebuffer, with the given byte order, filled with the
     * given string. The bytes of the string will be obtained by calling
     * getBytes().
     * 
     * @param text The string to fill the buffer.
     * @param order The order of the newly created buffer.
     * @return The bytebuffer.
     */
    public static ByteBuffer createFromString(String text, ByteOrder order)
    {
        byte[] buf = text.getBytes();
        return (ByteBuffer) ByteBuffer.allocate(buf.length).order(order).put(
                buf).flip();
    }
}