package org.qcri.micromappers.repository;

import org.qcri.micromappers.entity.Collaborator;
import org.springframework.data.repository.CrudRepository;

public abstract interface CollaboratorRepository extends CrudRepository<Collaborator, Long>
{
}
