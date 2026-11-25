/*
 * Copyright (c) 2002-2021, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.unittreeuserassignment.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.business.user.AdminUser;

/**
 * Agents unassigned from a resource when its unit changes
 */
public class ChangeUnitEventHandlerTest
{
    private static AdminUser user( int nId )
    {
        AdminUser user = new AdminUser( );
        user.setUserId( nId );
        return user;
    }

    /**
     * Transfer to another unit: the agents of the old unit leave the resource, the other agents stay
     */
    @Test
    public void testTransferToAnotherUnit( )
    {
        assertEquals( List.of( 1 ), ChangeUnitEventHandler.findUsersToUnassign( List.of( user( 1 ), user( 7 ) ), List.of( 1, 2 ), List.of( 3 ) ) );
    }

    /**
     * Transfer (or creation) to the unit the resource already has: its agent keeps the resource
     */
    @Test
    public void testTransferToTheSameUnit( )
    {
        assertEquals( Collections.emptyList( ), ChangeUnitEventHandler.findUsersToUnassign( List.of( user( 1 ) ), List.of( 1, 2 ), List.of( 1, 2 ) ) );
    }

    /**
     * Transfer to a unit the agent also belongs to: the agent keeps the resource
     */
    @Test
    public void testAgentMemberOfBothUnits( )
    {
        assertEquals( Collections.emptyList( ), ChangeUnitEventHandler.findUsersToUnassign( List.of( user( 1 ) ), List.of( 1, 2 ), List.of( 1, 5 ) ) );
    }

    /**
     * Unassignment (no new unit): the agents of the old unit leave the resource
     */
    @Test
    public void testUnassignment( )
    {
        assertEquals( List.of( 1 ), ChangeUnitEventHandler.findUsersToUnassign( List.of( user( 1 ) ), List.of( 1 ), Collections.emptyList( ) ) );
    }
}
