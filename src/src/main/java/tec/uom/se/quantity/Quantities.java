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

import static tec.uom.se.format.FormatBehavior.LOCALE_NEUTRAL;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParsePosition;
import java.util.Objects;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.ParserException;

import tec.uom.se.ComparableQuantity;
import tec.uom.se.format.QuantityFormat;

/**
 * Singleton class for accessing {@link Quantity} instances.
 * 
 * @author werner
 * @author otaviojava
 */
public final class Quantities {
  /**
   * Private singleton constructor.
   */
  private Quantities() {
  }

  /**
   * Returns the {@link #valueOf(java.math.BigDecimal, javax.measure.unit.Unit) decimal} measure of unknown type corresponding to the specified
   * representation. This method can be used to parse dimensionless quantities.<br/>
   * <code>
   *     Quantity<Dimensionless> proportion = Quantities.getQuantity("0.234").asType(Dimensionless.class);
   * </code>
   *
   * <p>
   * Note: This method handles only {@link javax.measure.unit.UnitFormat#getStandard standard} unit format (<a
   * href="http://unitsofmeasure.org/">UCUM</a> based). Locale-sensitive measure formatting and parsing are handled by the {@link MeasurementFormat}
   * class and its subclasses.
   * </p>
   *
   * @param csq
   *          the decimal value and its unit (if any) separated by space(s).
   * @return <code>QuantityFormat.getInstance(LOCALE_NEUTRAL).parse(csq, new ParsePosition(0))</code>
   */
  public static ComparableQuantity<?> getQuantity(CharSequence csq) {
    try {
      return QuantityFormat.getInstance(LOCALE_NEUTRAL).parse(csq, new ParsePosition(0));
    } catch (ParserException e) {
      throw new IllegalArgumentException(e.getParsedString());
    }
  }

  /**
   * Returns the scalar measurement. When the {@link Number} was {@link BigDecimal} or {@link BigInteger} will uses {@link DecimalQuantity}, when the
   * {@link Number} was {@link Double} will {@link DoubleQuantity} otherwise will {@link NumberQuantity}. in the specified unit.
   * 
   * @param value
   *          the measurement value.
   * @param unit
   *          the measurement unit.
   * @return the corresponding <code>numeric</code> measurement.
   * @throws NullPointerException
   *           when value or unit were null
   */
  public static <Q extends Quantity<Q>> ComparableQuantity<Q> getQuantity(Number value, Unit<Q> unit) {

    Objects.requireNonNull(value);
    Objects.requireNonNull(unit);
    if (Double.class.isInstance(value)) {
      return new DoubleQuantity<>(Double.class.cast(value), unit);
    } else if (BigDecimal.class.isInstance(value)) {
      return new DecimalQuantity<>(BigDecimal.class.cast(value), unit);
    } else if (BigInteger.class.isInstance(value)) {
      return new DecimalQuantity<>(new BigDecimal(BigInteger.class.cast(value)), unit);
    }
    return new NumberQuantity<>(value, unit);
  }
}
