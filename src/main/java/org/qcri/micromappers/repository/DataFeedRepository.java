package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.DataFeed;
import org.qcri.micromappers.utility.CollectionType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public abstract interface DataFeedRepository extends CrudRepository<DataFeed, Long>
{
	public DataFeed findByProviderAndFeedIdAndParentFeedIsNull(CollectionType provider, String feedId);

	public List<DataFeed> findByComputerVisionEnabled(boolean computerVisionEnabled);
}
