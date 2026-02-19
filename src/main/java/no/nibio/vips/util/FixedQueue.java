/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
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
 * 
 */
package no.nibio.vips.util;

import java.util.LinkedList;

/**
 * Facilitates a queue with a fixed size. If one adds an extra element, the
 * first inserted element is popped off the queue
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class FixedQueue<E> extends LinkedList<E>{

    private int maxSize;

    public FixedQueue(int maxSize)
    {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(E e)
    {
        boolean retVal = super.add(e);
        if(this.size() > this.maxSize)
            this.pop();
        return retVal;
    }
}
