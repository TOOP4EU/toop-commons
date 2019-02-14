package eu.toop.commons.usecase.regorg;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.id.IHasID;
import com.helger.commons.lang.EnumHelper;

/**
 * Difference between legal entity and ERegOrgEntityType.
 *
 * @author Philip Helger
 * @since 0.10.0
 */
public enum ERegOrgEntityType implements IHasID <String>
{
  LEGAL_ENTITY ("LE"),
  NATURAL_PERSON ("NP");

  private final String m_sID;

  private ERegOrgEntityType (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nullable
  public static ERegOrgEntityType getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (ERegOrgEntityType.class, sID);
  }
}
