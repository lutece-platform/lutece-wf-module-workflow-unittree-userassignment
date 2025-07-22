package fr.paris.lutece.plugins.workflow.modules.unittreeuserassignment.util;

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
        for ( UnitAssignment assignment : event.getOldAssignmentList( ) )
        {
            List<AdminUser> userList = _resourceUserDAO.selectUserListByResource( assignment.getIdResource( ), assignment.getResourceType( ), plugin );

            List<Integer> userIdList = UnitHome.findIdsUser( assignment.getIdAssignedUnit( ) );

            for ( AdminUser user : userList )
            {
                if ( userIdList.contains( user.getUserId( ) ) )
                {
                    _resourceUserDAO.deactivateByUserResource( user.getUserId( ), assignment.getIdResource( ), assignment.getResourceType( ), plugin );
                }
            }

        }
    }

}
