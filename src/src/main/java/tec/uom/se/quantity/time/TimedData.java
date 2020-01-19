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
package tec.uom.se.quantity.time;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Supplier;

import tec.uom.lib.common.function.Nameable;

/**
 * TimedData is a container for a data value that keeps track of its age. This class keeps track of the birth time of a bit of data, i.e. time the
 * object is instantiated.<br/>
 * The TimedData MUST be immutable.
 * 
 * @param <T>
 *          The data value.
 * 
 * @author <a href="mailto:units@catmedia.us">Werner Keil</a>
 * @version 0.5
 * @see <a href="http://en.wikipedia.org/wiki/Time_series"> Wikipedia: Time Series</a>
 */
public class TimedData<T> implements Nameable, Supplier<T> {
  private final T value;
  private final long timestamp;
  private final Instant instant;
  private String name;

  /**
   * Construct an instance of TimedData with a value and timestamp.
   *
   * @param data
   *          The value of the TimedData.
   * @param time
   *          The timestamp of the TimedData.
   */
  protected TimedData(T value, long time) {
    this.value = value;
    this.timestamp = time;
    this.instant = Instant.ofEpochMilli(time);
  }

  /**
   * Returns an {@code MeasurementRange} with the specified values.
   *
   * @param <T>
   *          the class of the value
   * @param val
   *          The minimum value for the measurement range.
   * @param time
   *          The maximum value for the measurement range.
   * @return an {@code MeasurementRange} with the given values
   */
  public static <T> TimedData<T> of(T val, long time) {
    return new TimedData<>(val, time);
  }

  /**
   * Returns the time with which this TimedData was created.
   * 
   * @return the time of creation
   */
  public long getTimestamp() {
    return timestamp;
  }

  public String getName() {
    return name;
  }

  // @Override
  public T get() {
    return value;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals()
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof TimedData<?>) {
      @SuppressWarnings("unchecked")
      final TimedData<T> other = (TimedData<T>) obj;
      return Objects.equals(get(), other.get()) && Objects.equals(getTimestamp(), other.getTimestamp()) && Objects.equals(getName(), other.getName());

    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hash(value, name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder().append("data= ").append(get()).append(", timestamp= ").append(getTimestamp());
    if (name != null && name.length() > 0) {
      sb.append(", name= ").append(getName());
    }
    return sb.toString();
  }

  public Instant getInstant() {
    return instant;
  }

}
