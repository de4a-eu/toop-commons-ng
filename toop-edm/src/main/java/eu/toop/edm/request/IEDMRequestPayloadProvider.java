package eu.toop.edm.request;

import javax.annotation.Nonnull;

import eu.toop.regrep.slot.ISlotProvider;

public interface IEDMRequestPayloadProvider
{
  @Nonnull
  ISlotProvider getAsSlotProvider ();
}
