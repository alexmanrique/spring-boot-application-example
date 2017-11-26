package com.myapp.dataaccessobject;

import com.myapp.domainobject.DriverDO;
import com.myapp.domainvalue.OnlineStatus;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface DriverRepository extends CrudRepository<DriverDO, Long>
{

    List<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus);
}
