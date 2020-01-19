/*
 * Units of Measurement Implementation for Java SE
 * Copyright (c) 2005-2017, Jean-Marie Dautelle, Werner Keil, V2COM.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-363 nor the names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tec.uom.se.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class LogConverterTest {

  private LogConverter logConverterBase10;

  @Before
  public void setUp() throws Exception {
    logConverterBase10 = new LogConverter(10.);
  }

  @Test
  public void testBaseUnmodified() {
    assertEquals(10., logConverterBase10.getBase(), 0.);
  }

  @Test
  public void testEqualityOfTwoLogConverter() {
    LogConverter logConverter = new LogConverter(10.);
    assertTrue(logConverter.equals(logConverterBase10));
    assertTrue(!logConverter.equals(null));
  }

  @Test
  public void testGetValueLogConverter() {
    LogConverter logConverter = new LogConverter(Math.E);
    assertEquals("Log(10.0)", logConverterBase10.getValue());
    assertEquals("ln", logConverter.getValue());
  }

  @Test
  public void isLinearOfLogConverterTest() {
    assertTrue(!logConverterBase10.isLinear());
  }

  @Test
  public void convertLogTest() {
    assertEquals(1, logConverterBase10.convert(10), 0.);
    assertEquals(Double.NaN, logConverterBase10.convert(-10), 0.);
    assertTrue(Double.isInfinite(logConverterBase10.convert(0)));
  }

  @Test
  public void inverseLogTest() {
    LogConverter logConverter = new LogConverter(Math.E);
    assertEquals(new ExpConverter(10.), logConverterBase10.inverse());
    assertEquals(new ExpConverter(Math.E), logConverter.inverse());
  }
}
