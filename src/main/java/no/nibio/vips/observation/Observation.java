/*
 * Copyright (c) 2015 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package no.nibio.vips.observation;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

/**
 * @copyright 2015-2023 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
@JsonDeserialize(as = ObservationImpl.class)
public interface Observation extends Comparable {
    
    public final static Integer STATUS_TYPE_ID_PENDING = 1;
    public final static Integer STATUS_TYPE_ID_REJECTED = 2;
    public final static Integer STATUS_TYPE_ID_APPROVED = 3;
    
    public Date getTimeOfObservation();
    public String getGeoinfo(); 
    public String getObservationData();
    public String getName();

}
