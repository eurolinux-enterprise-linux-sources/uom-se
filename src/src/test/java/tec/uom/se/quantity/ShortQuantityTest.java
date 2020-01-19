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
package tec.uom.se.quantity;

import static org.junit.Assert.assertEquals;

import javax.measure.Quantity;
import javax.measure.quantity.ElectricResistance;
import javax.measure.quantity.Time;

import org.junit.Assert;
import org.junit.Test;

import tec.uom.se.unit.Units;

public class ShortQuantityTest {

  @Test
  public void divideTest() {
    ShortQuantity<ElectricResistance> quantity1 = new ShortQuantity<>(Short.valueOf("3").shortValue(), Units.OHM);
    ShortQuantity<ElectricResistance> quantity2 = new ShortQuantity<>(Short.valueOf("2").shortValue(), Units.OHM);
    Quantity<?> result = quantity1.divide(quantity2);
    assertEquals(Double.valueOf("1"), result.getValue());
  }

  @Test
  public void addTest() {
    ShortQuantity<ElectricResistance> quantity1 = new ShortQuantity<>(Short.valueOf("1").shortValue(), Units.OHM);
    ShortQuantity<ElectricResistance> quantity2 = new ShortQuantity<>(Short.valueOf("2").shortValue(), Units.OHM);
    Quantity<ElectricResistance> result = quantity1.add(quantity2);
    assertEquals(Short.valueOf("3").intValue(), result.getValue().intValue());
  }

  @Test
  public void subtractTest() {
    ShortQuantity<ElectricResistance> quantity1 = new ShortQuantity<>(Short.valueOf("1").shortValue(), Units.OHM);
    ShortQuantity<ElectricResistance> quantity2 = new ShortQuantity<>(Short.valueOf("2").shortValue(), Units.OHM);
    Quantity<ElectricResistance> result = quantity2.subtract(quantity1);
    assertEquals(Short.valueOf("1").intValue(), result.getValue().intValue());
    assertEquals(Units.OHM, result.getUnit());
  }

  @Test
  public void multiplyQuantityTest() {
    ShortQuantity<ElectricResistance> quantity1 = new ShortQuantity<>(Short.valueOf("3").shortValue(), Units.OHM);
    ShortQuantity<ElectricResistance> quantity2 = new ShortQuantity<>(Short.valueOf("2").shortValue(), Units.OHM);
    Quantity<?> result = quantity1.multiply(quantity2);
    assertEquals(6.0, result.getValue());
  }

  @Test
  public void longValueTest() {
    ShortQuantity<Time> day = new ShortQuantity<Time>(Short.valueOf("3").shortValue(), Units.DAY);
    long hours = day.longValue(Units.HOUR);
    assertEquals(72L, hours);
  }

  @Test
  public void doubleValueTest() {
    ShortQuantity<Time> day = new ShortQuantity<Time>(Short.valueOf("3").shortValue(), Units.DAY);
    double hours = day.doubleValue(Units.HOUR);
    assertEquals(72D, hours, 0);
  }

  @Test
  public void toTest() {
    Quantity<Time> day = Quantities.getQuantity(1D, Units.DAY);
    Quantity<Time> hour = day.to(Units.HOUR);
    Assert.assertEquals(hour.getValue().intValue(), 24);
    Assert.assertEquals(hour.getUnit(), Units.HOUR);

    Quantity<Time> dayResult = hour.to(Units.DAY);
    Assert.assertEquals(dayResult.getValue().intValue(), day.getValue().intValue());
    Assert.assertEquals(dayResult.getValue().intValue(), day.getValue().intValue());
  }
}
