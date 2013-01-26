/*
 *  PCF font reader
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.liaquay.tinyx.pcf;

import java.io.IOException;
import java.io.InputStream;

import com.liaquay.tinyx.util.IntMap;

public class PcfFontFactory {
	
	private final static int MAGIC = 1885562369;
	
	public static PcfFont read(final InputStream inputStream) throws IOException {
		
		final PcfInputStream pcfInputStream = new PcfInputStream(inputStream);
		
		final int version = pcfInputStream.readLsbInt();
		if (version != MAGIC) {
			throw new IOException("Bad version "+version);
		}
		
		final int tocEntries = pcfInputStream.readLsbInt();
		if (tocEntries > MAGIC) {
			throw new IOException("TOC count "+tocEntries+" too large");
		}
		
		final PcfToc tableOfContents = new PcfToc();
		
		for(int i = 0; i < tocEntries; ++i) {
			tableOfContents.add(readTableOfContentsEntry(pcfInputStream));
		}
		
		PcfAccelerators accelerators = null;
		if(tableOfContents.getAccelerators() != null) {
			accelerators = readAccelerators(pcfInputStream, tableOfContents.getAccelerators());
		}
		
		final PcfMetrics[] metrics = readMetrics(pcfInputStream, tableOfContents.getMetrics());
		final PcfBitmaps bitmaps = readBitmaps(pcfInputStream, tableOfContents.getBitmaps());
		final PcfEncodings encodings = readEncodings(pcfInputStream, tableOfContents.getBdfEncodings());
		if(accelerators == null && tableOfContents.getBdfAccelerators() != null) {
			accelerators = readAccelerators(pcfInputStream, tableOfContents.getAccelerators());
		}
		if (accelerators == null) {
			throw new IOException("No accelerators");
		}
		
		return new PcfFont(accelerators, metrics, bitmaps, encodings);
	}
	
	private static PcfFormattedStream getFormattedStream(final PcfInputStream pcfInputStream, final int format) {
		if(PcfFormatType.MsbByteOrder.getBooleanValue(format)) {
			return new PcfMsbFormattedStream(pcfInputStream);
		}
		else {
			return new PcfLsbFormattedStream(pcfInputStream);
		}
	}
	
	private static PcfMetrics readMetric(final PcfFormattedStream formattedStream) throws IOException {
		final int leftSideBearing = formattedStream.readUnsignedShort(); // TODO Should this be signed?
		final int rightSideBearing = formattedStream.readUnsignedShort();
		final int characterWidth = formattedStream.readUnsignedShort();
		final int ascent = formattedStream.readSignedShort();
		final int descent = formattedStream.readSignedShort();
		final int attributes = formattedStream.readUnsignedShort();
		return new PcfMetrics(leftSideBearing, rightSideBearing, characterWidth, ascent, descent, attributes);
	}

	private static PcfMetrics readCompressedMetric(final PcfFormattedStream formattedStream) throws IOException {
		final int leftSideBearing = formattedStream.readUnsignedByte()-0x80; // TODO Should this be signed?
		final int rightSideBearing = formattedStream.readUnsignedByte()-0x80;
		final int characterWidth = formattedStream.readUnsignedByte()-0x80;
		final int ascent = formattedStream.readUnsignedByte()-0x80;
		final int descent = formattedStream.readUnsignedByte()-0x80;
		final int attributes = 0;
		return new PcfMetrics(leftSideBearing, rightSideBearing, characterWidth, ascent, descent, attributes);	}	
	
	private static PcfAccelerators readAccelerators(
			final PcfInputStream pcfInputStream,
			final PcfTocEntry tocEntry) throws IOException {
		
		pcfInputStream.seekAbsolute(tocEntry.getOffset());
		
		final int format = pcfInputStream.readLsbInt();
		
		final PcfFormattedStream formattedStream = getFormattedStream(pcfInputStream, format);
		
		final boolean noOverlap = formattedStream.readBoolean();
		final boolean constantMetrics = formattedStream.readBoolean();
		final boolean terminalFont = formattedStream.readBoolean();
		final boolean constantWidth = formattedStream.readBoolean();
		final boolean inkInside = formattedStream.readBoolean();
		final boolean inkMetrics = formattedStream.readBoolean();
		final boolean drawDirection = formattedStream.readBoolean();
		final boolean naturalAlignment = formattedStream.readBoolean();
		final int ascent = formattedStream.readInt();
		final int descent = formattedStream.readInt();
		final int maxOverlap = formattedStream.readInt();
		
		final PcfMetrics minBounds = readMetric(formattedStream);
		final PcfMetrics maxBounds = readMetric(formattedStream);
		final PcfMetrics minInkBounds;
		final PcfMetrics maxInkBounds;
		if(PcfFormatType.AccelWInkBounds.getBooleanValue(format)) {
			minInkBounds = readMetric(formattedStream);
			maxInkBounds = readMetric(formattedStream);
		}
		else {
			minInkBounds = minBounds;
			maxInkBounds = maxBounds;
		}
		
		return new PcfAccelerators(
				noOverlap, 
				constantMetrics, 
				terminalFont, 
				constantWidth, 
				inkInside, 
				inkMetrics, 
				drawDirection, 
				naturalAlignment, 
				ascent, 
				descent, 
				maxOverlap, 
				minBounds, 
				maxBounds,
				minInkBounds,
				maxInkBounds);
	}	
	
	private static PcfMetrics[] readMetrics( 
			final PcfInputStream pcfInputStream,
			final PcfTocEntry tocEntry) throws IOException {

		pcfInputStream.seekAbsolute(tocEntry.getOffset());

		final int format = pcfInputStream.readLsbInt();

		final PcfFormattedStream formattedStream = getFormattedStream(pcfInputStream, format);
		
		final int numberOfMetrics;
		if(PcfFormatType.CompressedMetrics.getBooleanValue(format)) {
			numberOfMetrics = formattedStream.readUnsignedShort();
		}
		else {
			numberOfMetrics = formattedStream.readInt();
		}
		
		final PcfMetrics[] metrics = new PcfMetrics[numberOfMetrics];
		
		for (int i=0; i< numberOfMetrics; i++) {
			if(PcfFormatType.CompressedMetrics.getBooleanValue(format)) {
				metrics[i] = readCompressedMetric(formattedStream);
			}
			else {
				metrics[i] = readMetric(formattedStream);
			}
		}
		
		return metrics;
	}	

	private static PcfBitmaps readBitmaps( 
			final PcfInputStream pcfInputStream,
			final PcfTocEntry tocEntry) throws IOException {

		pcfInputStream.seekAbsolute(tocEntry.getOffset());

		final int format = pcfInputStream.readLsbInt();
		final boolean msbBitOrder = PcfFormatType.MsbBitOrder.getBooleanValue(format);
		final boolean msbByteOrder = PcfFormatType.MsbByteOrder.getBooleanValue(format);

		final PcfFormattedStream formattedStream = getFormattedStream(pcfInputStream, format);
		
		final int scanUnit = 1 << PcfFormatType.ScanUnit.getValue(format);
		final int numberOfBitmaps = formattedStream.readInt();

		final int[] offsets = new int[numberOfBitmaps];
		
		for (int i=0; i<numberOfBitmaps; i++) {
			offsets[i] = formattedStream.readInt();
		}
		
		final int bitmapSizes[] = new int[GLYPHPADOPTIONS];
		for (int i=0; i<GLYPHPADOPTIONS; i++) {
			bitmapSizes[i] = formattedStream.readInt();
		}
		
		final int glyphPadIndex = PcfFormatType.GlyphPad.getValue(format);
		final int glyphPadding = 1 << glyphPadIndex;
		final int sizebitmaps = bitmapSizes[glyphPadIndex];
		final byte[] data = new byte[sizebitmaps + scanUnit];
		pcfInputStream.read(data);
		
		return new PcfBitmaps(msbBitOrder, msbByteOrder, glyphPadding, scanUnit, offsets, data);
	}
	
	private static PcfEncodings readEncodings(
			final PcfInputStream pcfInputStream,
			final PcfTocEntry tocEntry) throws IOException {

		pcfInputStream.seekAbsolute(tocEntry.getOffset());

		final int format = pcfInputStream.readLsbInt();

		final PcfFormattedStream formattedStream = getFormattedStream(pcfInputStream, format);

		final int firstCol = formattedStream.readUnsignedShort();
		final int lastCol = formattedStream.readUnsignedShort();
		final int firstRow = formattedStream.readUnsignedShort();
		final int lastRow = formattedStream.readUnsignedShort();
		final int defaultCharacter = formattedStream.readUnsignedShort();

		final IntMap<Integer> characterMap = new IntMap<Integer>();
		int minChar = 0xffff;
		int maxChar = 0;
		for (int r=firstRow; r<=lastRow; r++) {
			for (int c=firstCol; c<=lastCol; c++) {
				final int map = formattedStream.readUnsignedShort();
				if (map != 0xffff) {
					final int charIndex = (r<<8)|c;
					characterMap.put(charIndex, map);
					if(charIndex < minChar) minChar = charIndex;
					if(charIndex > maxChar) maxChar = charIndex;
				}
			}
		}
		
		return new PcfEncodings(firstCol, lastCol, firstRow, lastRow, defaultCharacter, characterMap, minChar, maxChar);
	}
	
	private final static int GLYPHPADOPTIONS = 4;

	private static PcfTocEntry readTableOfContentsEntry(final PcfInputStream pcfInputStream) throws IOException {
		return new PcfTocEntry(
				pcfInputStream.readLsbInt(),
				pcfInputStream.readLsbInt(),
				pcfInputStream.readLsbInt(),
				pcfInputStream.readLsbInt());
	}
}
