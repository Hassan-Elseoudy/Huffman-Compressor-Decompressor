package pkjaya;

/******************************************************************************
 *  Compilation:  javac BinaryOut.java
 *  Execution:    java BinaryOut
 *  Dependencies: none
 *
 *  Write binary data to an output stream, either one 1-bit boolean,
 *  one 8-bit char, one 32-bit int, one 64-bit double, one 32-bit float,
 *  or one 64-bit long at a time. The output stream can be standard
 *  output, a file, an OutputStream or a Socket.
 *
 *  The bytes written are not aligned.
 *
 ******************************************************************************/

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <i>Binary output</i>. This class provides methods for converting primtive
 * type variables ({@code boolean}, {@code byte}, {@code char}, {@code int},
 * {@code long}, {@code float}, and {@code double}) to sequences of bits and
 * writing them to an output stream. The output stream can be standard output, a
 * file, an OutputStream or a Socket. Uses big-endian (most-significant byte
 * first).
 * <p>
 * The client must {@code flush()} the output stream when finished writing bits.
 * <p>
 * The client should not intermix calls to {@code BinaryOut} with calls to
 * {@code Out}; otherwise unexpected behavior will result.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public final class BinaryOut {

	private BufferedOutputStream out; // the output stream
	private int buffer; // 8-bit buffer of bits to write out
	private int n; // number of bits remaining in buffer

	/**
	 * Initializes a binary output stream from a file.
	 * 
	 * @param filename the name of the file
	 */
	public BinaryOut(String filename) {
		try {
			out = new BufferedOutputStream(new FileOutputStream(filename,true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the specified bit to the binary output stream.
	 * 
	 * @param x the bit
	 */
	private void writeBit(boolean x) {
		// add bit to buffer
		buffer <<= 1;
		if (x)
			buffer |= 1;

		// if buffer is full (8 bits), write out as a single byte
		n++;
		if (n == 8)
			clearBuffer();
	}

	// write out any remaining bits in buffer to the binary output stream, padding
	// with 0s
	private void clearBuffer() {
		if (n == 0)
			return;
		if (n > 0)
			buffer <<= (8 - n);
		try {
			out.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		n = 0;
		buffer = 0;
	}

	/**
	 * Flushes the binary output stream, padding 0s if number of bits written so far
	 * is not a multiple of 8.
	 */
	public void flush() {
		clearBuffer();
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Flushes and closes the binary output stream. Once it is closed, bits can no
	 * longer be written.
	 */
	public void close() {
		flush();
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the specified bit to the binary output stream.
	 * 
	 * @param x the {@code boolean} to write
	 */
	public void write(boolean x) {
		writeBit(x);
	}

}
