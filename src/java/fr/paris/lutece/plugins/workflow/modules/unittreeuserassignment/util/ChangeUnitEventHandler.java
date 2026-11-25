package fr.paris.lutece.plugins.workflow.modules.unittreeuserassignment.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.inject.Inject;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.plugins.userassignment.business.IResourceUserDAO;
import fr.paris.lutece.plugins.workflow.modules.unittree.util.ChangeUnitEvent;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Named;

/**
 * Event handler for {@link ChangeUnitEvent}
 */
@ApplicationScoped
@Named( "unittreeuserassignment.changeUnitEventHandler" )
public class ChangeUnitEventHandler
{

    @Inject
    private IResourceUserDAO _resourceUserDAO;

    public void onApplicationEvent( @Observes ChangeUnitEvent event )
    {
        final Plugin plugin = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );
        UnitAssignment newAssignment = event.getNewAssignment( );
        List<Integer> newUnitUserIdList = newAssignment == null ? Collections.emptyList( ) : UnitHome.findIdsUser( newAssignment.getIdAssignedUnit( ) );

        for ( UnitAssignment assignment : event.getOldAssignmentList( ) )
        {
            List<AdminUser> userList = _resourceUserDAO.selectUserListByResource( assignment.getIdResource( ), assignment.getResourceType( ), plugin );

            List<Integer> userIdList = UnitHome.findIdsUser( assignment.getIdAssignedUnit( ) );

            for ( Integer nIdUser : findUsersToUnassign( userList, userIdList, newUnitUserIdList ) )
            {
                _resourceUserDAO.deactivateByUserResource( nIdUser, assignment.getIdResource( ), assignment.getResourceType( ), plugin );
            }
        }
    }

    /**
     * The agents of the resource who leave it with the unit: members of the old unit who are not members of the unit that
     * receives the resource (a transfer to the same unit, or to a unit the agent also belongs to, keeps the agent)
     * 
     * @param listAssignedUser
     *            the agents assigned to the resource
     * @param listOldUnitUserId
     *            the members of the old unit
     * @param listNewUnitUserId
     *            the members of the unit that receives the resource (empty for an unassignment)
     * @return the ids of the agents to unassign
     */
    static List<Integer> findUsersToUnassign( List<AdminUser> listAssignedUser, List<Integer> listOldUnitUserId, List<Integer> listNewUnitUserId )
    {
        List<Integer> listUserToUnassign = new ArrayList<>( );
        for ( AdminUser user : listAssignedUser )
        {
            if ( listOldUnitUserId.contains( user.getUserId( ) ) && !listNewUnitUserId.contains( user.getUserId( ) ) )
            {
                listUserToUnassign.add( user.getUserId( ) );
            }
        }
        return listUserToUnassign;
    }

}
