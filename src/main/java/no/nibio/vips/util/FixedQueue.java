/*
 * Copyright (c) 2016 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the NIBIO Open Source License as published by 
 * NIBIO, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NIBIO Open Source License for more details.
 * 
 * You should have received a copy of the NIBIO Open Source License
 * along with VIPSCommon.  If not, see <http://www.nibio.no/licenses/>.
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
