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
package tec.uom.se;

import tec.uom.se.function.AddConverter;
import tec.uom.se.function.MultiplyConverter;
import tec.uom.se.function.RationalConverter;
import tec.uom.se.quantity.QuantityDimension;
import tec.uom.se.spi.DimensionalModel;
import tec.uom.se.unit.AlternateUnit;
import tec.uom.se.unit.AnnotatedUnit;
import tec.uom.se.unit.ProductUnit;
import tec.uom.se.unit.TransformedUnit;
import tec.uom.se.unit.Units;

import javax.measure.*;
import javax.measure.quantity.Dimensionless;
import javax.measure.spi.ServiceProvider;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 * The class represents units founded on the seven <b>SI</b> base units for seven base quantities assumed to be mutually independent.
 * </p>
 *
 * <p>
 * For all physics units, unit conversions are symmetrical: <code>u1.getConverterTo(u2).equals(u2.getConverterTo(u1).inverse())</code>. Non-physical
 * units (e.g. currency units) for which conversion is not symmetrical should have their own separate class hierarchy and are considered distinct
 * (e.g. financial units), although they can always be combined with physics units (e.g. "€/Kg", "$/h").
 * </p>
 *
 * @see <a href= "http://en.wikipedia.org/wiki/International_System_of_Units">Wikipedia: International System of Units</a>
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @author <a href="mailto:units@catmedia.us">Werner Keil</a>
 * @version 1.0.3, February 24, 2017
 * @since 1.0
 */
public abstract class AbstractUnit<Q extends Quantity<Q>> implements Unit<Q>, Comparable<Unit<Q>>, Serializable {

  /**
     * 
     */
  private static final long serialVersionUID = -4344589505537030204L;

  /**
   * Holds the dimensionless unit <code>ONE</code>.
   */
  public static final Unit<Dimensionless> ONE = new ProductUnit<>();

  /**
   * Holds the name.
   */
  protected String name;

  /**
   * Holds the symbol.
   */
  private String symbol;

  /**
   * Holds the unique symbols collection (base units or alternate units).
   */
  protected static final Map<String, Unit<?>> SYMBOL_TO_UNIT = new HashMap<>();

  /**
   * DefaultQuantityFactory constructor.
   */
  protected AbstractUnit() {
  }

  protected Type getActualType() {
    ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
    return parameterizedType.getActualTypeArguments()[0].getClass().getGenericInterfaces()[0];
  }

  /**
   * Indicates if this unit belongs to the set of coherent SI units (unscaled SI units).
   * 
   * The base and coherent derived units of the SI form a coherent set, designated the set of coherent SI units. The word coherent is used here in the
   * following sense: when coherent units are used, equations between the numerical values of quantities take exactly the same form as the equations
   * between the quantities themselves. Thus if only units from a coherent set are used, conversion factors between units are never required.
   * 
   * @return <code>equals(toSystemUnit())</code>
   */
  public boolean isSystemUnit() {
    AbstractUnit<Q> si = this.toSystemUnit();
    return (this == si) || this.equals(si);
  }

  /**
   * Returns the unscaled {@link SI} unit from which this unit is derived.
   * 
   * They SI unit can be be used to identify a quantity given the unit. For example:[code] static boolean isAngularVelocity(AbstractUnit<?> unit) {
   * return unit.toSI().equals(RADIAN.divide(SECOND)); } assert(REVOLUTION.divide(MINUTE).isAngularVelocity()); // Returns true. [/code]
   *
   * @return the unscaled metric unit from which this unit is derived.
   */
  protected abstract AbstractUnit<Q> toSystemUnit();

  /**
   * Returns the converter from this unit to its unscaled {@link #toSysemUnit System Unit} unit.
   *
   * @return <code>getConverterTo(this.toSystemUnit())</code>
   * @see #toSI
   */
  public abstract UnitConverter getSystemConverter();

  /**
   * Annotates the specified unit. Annotation does not change the unit semantic. Annotations are often written between curly braces behind units. For
   * example: [code] AbstractUnit<Volume> PERCENT_VOL = Units.PERCENT.annotate("vol"); // "%{vol}" AbstractUnit<Mass> KG_TOTAL =
   * Units.KILOGRAM.annotate("total"); // "kg{total}" AbstractUnit<Dimensionless> RED_BLOOD_CELLS = Units.ONE.annotate("RBC"); // "{RBC}" [/code]
   *
   * Note: Annotation of system units are not considered themselves as system units.
   *
   * @param annotation
   *          the unit annotation.
   * @return the annotated unit.
   */
  public AnnotatedUnit<Q> annotate(String annotation) {
    return new AnnotatedUnit<>(this, annotation);
  }

  /**
   * Returns the abstract unit represented by the specified characters as per standard <a href="http://www.unitsofmeasure.org/">UCUM</a> format.
   *
   * Locale-sensitive unit parsing should be handled using the OSGi {@link org.unitsofmeasurement.service.UnitFormatService} or for non-OSGi
   * applications the {@link LocalUnitFormat} utility class.
   *
   * <p>
   * Note: The standard format supports dimensionless units.[code] AbstractUnit<Dimensionless> PERCENT =
   * AbstractUnit.parse("100").inverse().asType(Dimensionless.class); [/code]
   * </p>
   *
   * @param charSequence
   *          the character sequence to parse.
   * @return <code>UCUMFormat.getCaseSensitiveInstance().parse(csq, new ParsePosition(0))</code>
   * @throws ParserException
   *           if the specified character sequence cannot be correctly parsed (e.g. not UCUM compliant).
   */
  public static Unit<?> parse(CharSequence charSequence) {
    return ServiceProvider.current().getUnitFormatService().getUnitFormat().parse(charSequence);
  }

  /**
   * Returns the standard representation of this physics unit. The string produced for a given unit is always the same; it is not affected by the
   * locale. It can be used as a canonical string representation for exchanging units, or as a key for a Hashtable, etc.
   *
   * Locale-sensitive unit parsing should be handled using the {@link org.unitsofmeasurement.service.UnitFormat} service.
   *
   * @return <code>ServiceProvider.current().getUnitFormatService().getUnitFormat().format(this)</code>
   */
  @Override
  public String toString() {
    return ServiceProvider.current().getUnitFormatService().getUnitFormat().format(this);
  }

  // ///////////////////////////////////////////////////////
  // Implements org.unitsofmeasurement.Unit<Q> interface //
  // ///////////////////////////////////////////////////////

  /**
   * Returns the system unit (unscaled SI unit) from which this unit is derived. They can be be used to identify a quantity given the unit. For
   * example:[code] static boolean isAngularVelocity(AbstractUnit<?> unit) { return unit.getSystemUnit().equals(RADIAN.divide(SECOND)); }
   * assert(REVOLUTION.divide(MINUTE).isAngularVelocity()); // Returns true. [/code]
   *
   * @return the unscaled metric unit from which this unit is derived.
   */
  @Override
  public final AbstractUnit<Q> getSystemUnit() {
    return toSystemUnit();
  }

  /**
   * Indicates if this unit is compatible with the unit specified. To be compatible both units must be physics units having the same fundamental
   * dimension.
   *
   * @param that
   *          the other unit.
   * @return <code>true</code> if this unit and that unit have equals fundamental dimension according to the current physics model; <code>false</code>
   *         otherwise.
   */
  @Override
  public final boolean isCompatible(Unit<?> that) {
    if ((this == that) || this.equals(that))
      return true;
    if (!(that instanceof AbstractUnit))
      return false;
    Dimension thisDimension = this.getDimension();
    Dimension thatDimension = that.getDimension();
    if (thisDimension.equals(thatDimension))
      return true;
    DimensionalModel model = DimensionalModel.current(); // Use
    // dimensional
    // analysis
    // model.
    return model.getFundamentalDimension(thisDimension).equals(model.getFundamentalDimension(thatDimension));
  }

  /**
   * Casts this unit to a parameterized unit of specified nature or throw a ClassCastException if the dimension of the specified quantity and this
   * unit's dimension do not match (regardless whether or not the dimensions are independent or not).
   *
   * @param type
   *          the quantity class identifying the nature of the unit.
   * @throws ClassCastException
   *           if the dimension of this unit is different from the SI dimension of the specified type.
   * @see Units#getUnit(Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public final <T extends Quantity<T>> AbstractUnit<T> asType(Class<T> type) {
    Dimension typeDimension = QuantityDimension.of(type);
    if ((typeDimension != null) && (!typeDimension.equals(this.getDimension())))
      throw new ClassCastException("The unit: " + this + " is not compatible with quantities of type " + type);
    return (AbstractUnit<T>) this;
  }

  @Override
  public abstract Map<? extends Unit<?>, Integer> getBaseUnits();

  @Override
  public abstract Dimension getDimension();

  protected void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getSymbol() {
    return symbol;
  }

  protected void setSymbol(String s) {
    this.symbol = s;
  }

  @Override
  public final UnitConverter getConverterTo(Unit<Q> that) throws UnconvertibleException {
    if ((this == that) || this.equals(that))
      return AbstractConverter.IDENTITY; // Shortcut.
    Unit<Q> thisSystemUnit = this.getSystemUnit();
    Unit<Q> thatSystemUnit = that.getSystemUnit();
    if (!thisSystemUnit.equals(thatSystemUnit))
      try {
        return getConverterToAny(that);
      } catch (IncommensurableException e) {
        throw new UnconvertibleException(e);
      }
    UnitConverter thisToSI = this.getSystemConverter();
    UnitConverter thatToSI = that.getConverterTo(thatSystemUnit);
    return thatToSI.inverse().concatenate(thisToSI);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public final UnitConverter getConverterToAny(Unit<?> that) throws IncommensurableException, UnconvertibleException {
    if (!isCompatible(that))
      throw new IncommensurableException(this + " is not compatible with " + that);
    AbstractUnit thatAbstr = (AbstractUnit) that; // Since both units are
    // compatible they must
    // be both physics
    // units.
    DimensionalModel model = DimensionalModel.current();
    AbstractUnit thisSystemUnit = this.getSystemUnit();
    UnitConverter thisToDimension = model.getDimensionalTransform(thisSystemUnit.getDimension()).concatenate(this.getSystemConverter());
    AbstractUnit thatSystemUnit = thatAbstr.getSystemUnit();
    UnitConverter thatToDimension = model.getDimensionalTransform(thatSystemUnit.getDimension()).concatenate(thatAbstr.getSystemConverter());
    return thatToDimension.inverse().concatenate(thisToDimension);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public final Unit<Q> alternate(String symbol) {
    return new AlternateUnit(this, symbol);
  }

  @Override
  public final AbstractUnit<Q> transform(UnitConverter operation) {
    AbstractUnit<Q> systemUnit = this.getSystemUnit();
    UnitConverter cvtr = this.getSystemConverter().concatenate(operation);
    if (cvtr.equals(AbstractConverter.IDENTITY))
      return systemUnit;
    return new TransformedUnit<>(systemUnit, cvtr);
  }

  @Override
  public final AbstractUnit<Q> shift(double offset) {
    if (offset == 0)
      return this;
    return transform(new AddConverter(offset));
  }

  @Override
  public final AbstractUnit<Q> multiply(double factor) {
    if (factor == 1)
      return this;
    if (isLongValue(factor))
      return transform(new RationalConverter(BigInteger.valueOf((long) factor), BigInteger.ONE));
    return transform(new MultiplyConverter(factor));
  }

  private static boolean isLongValue(double value) {
    return !((value < Long.MIN_VALUE) || (value > Long.MAX_VALUE)) && Math.floor(value) == value;
  }

  /**
   * Returns the product of this unit with the one specified.
   *
   * <p>
   * Note: If the specified unit (that) is not a physical unit, then <code>that.multiply(this)</code> is returned.
   * </p>
   *
   * @param that
   *          the unit multiplicand.
   * @return <code>this * that</code>
   */
  @Override
  public final Unit<?> multiply(Unit<?> that) {
    if (that instanceof AbstractUnit)
      return multiply((AbstractUnit<?>) that);
    // return that.multiply(this); // Commutatif.
    return ProductUnit.getProductInstance(this, that);
  }

  /**
   * Returns the product of this physical unit with the one specified.
   *
   * @param that
   *          the physical unit multiplicand.
   * @return <code>this * that</code>
   */
  public final Unit<?> multiply(AbstractUnit<?> that) {
    if (this.equals(ONE))
      return that;
    if (that.equals(ONE))
      return this;
    return ProductUnit.getProductInstance(this, that);
  }

  /**
   * Returns the inverse of this physical unit.
   *
   * @return <code>1 / this</code>
   */
  @Override
  public final Unit<?> inverse() {
    if (this.equals(ONE))
      return this;
    return ProductUnit.getQuotientInstance(ONE, this);
  }

  /**
   * Returns the result of dividing this unit by the specifified divisor. If the factor is an integer value, the division is exact. For example:
   * 
   * <pre>
   * <code>
   *    QUART = GALLON_LIQUID_US.divide(4); // Exact definition.
   * </code>
   * </pre>
   * 
   * @param divisor
   *          the divisor value.
   * @return this unit divided by the specified divisor.
   */
  @Override
  public final AbstractUnit<Q> divide(double divisor) {
    if (divisor == 1)
      return this;
    if (isLongValue(divisor))
      return transform(new RationalConverter(BigInteger.ONE, BigInteger.valueOf((long) divisor)));
    return transform(new MultiplyConverter(1.0 / divisor));
  }

  /**
   * Returns the quotient of this unit with the one specified.
   *
   * @param that
   *          the unit divisor.
   * @return <code>this.multiply(that.inverse())</code>
   */
  @Override
  public final Unit<?> divide(Unit<?> that) {
    return this.multiply(that.inverse());
  }

  /**
   * Returns the quotient of this physical unit with the one specified.
   *
   * @param that
   *          the physical unit divisor.
   * @return <code>this.multiply(that.inverse())</code>
   */
  public final Unit<?> divide(AbstractUnit<?> that) {
    return this.multiply(that.inverse());
  }

  /**
   * Returns a unit equals to the given root of this unit.
   *
   * @param n
   *          the root's order.
   * @return the result of taking the given root of this unit.
   * @throws ArithmeticException
   *           if <code>n == 0</code> or if this operation would result in an unit with a fractional exponent.
   */
  @Override
  public final Unit<?> root(int n) {
    if (n > 0)
      return ProductUnit.getRootInstance(this, n);
    else if (n == 0)
      throw new ArithmeticException("Root's order of zero");
    else
      // n < 0
      return ONE.divide(this.root(-n));
  }

  /**
   * Returns a unit equals to this unit raised to an exponent.
   *
   * @param n
   *          the exponent.
   * @return the result of raising this unit to the exponent.
   */
  @Override
  public final Unit<?> pow(int n) {
    if (n > 0)
      return this.multiply(this.pow(n - 1));
    else if (n == 0)
      return ONE;
    else
      // n < 0
      return ONE.divide(this.pow(-n));
  }

  /**
   * Compares this unit to the specified unit. The default implementation compares the name and symbol of both this unit and the specified unit.
   *
   * @return a negative integer, zero, or a positive integer as this unit is less than, equal to, or greater than the specified unit.
   */
  public int compareTo(Unit<Q> that) {
    if (name != null && getSymbol() != null) {
      return name.compareTo(that.getName()) + getSymbol().compareTo(that.getSymbol());
    } else if (name == null) {
      if (getSymbol() != null && that.getSymbol() != null) {
        return getSymbol().compareTo(that.getSymbol());
      } else {
        return -1;
      }
    } else if (getSymbol() == null) {
      if (name != null) {
        return name.compareTo(that.getName());
      } else {
        return -1;
      }
    } else {
      return -1;
    }
  }

  // //////////////////////////////////////////////////////////////
  // Ensures that sub-classes implements hashCode/equals method.
  // //////////////////////////////////////////////////////////////

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object that);
}