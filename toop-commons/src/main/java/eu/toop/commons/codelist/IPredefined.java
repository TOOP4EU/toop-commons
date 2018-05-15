package eu.toop.commons.codelist;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.id.IHasID;
import com.helger.commons.name.IHasName;
import com.helger.commons.version.Version;

/**
 * Base interface for predefined lists
 *
 * @author Philip Helger
 */
public interface IPredefined extends IHasID<String>, IHasName {
  /**
   *
   * @return The code list version when the item was added. Never
   *         <code>null</code>.
   */
  @Nonnull
  Version getSince ();

  /**
   *
   * @return <code>true</code> if this code list entry is deprecated in the code
   *         list.
   * @see #getDeprecatedSince()
   */
  boolean isDeprecated ();

  /**
   *
   * @return The code list version when the item was deprecated. May be
   *         <code>null</code>. Must not be <code>null</code> if
   *         {@link #isDeprecated()} returns <code>true</code>.
   * @see #isDeprecated()
   */
  @Nullable
  Version getDeprecatedSince ();
}
