package fr.paris.lutece.plugins.workflow.modules.unittreeuserassignment.util;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationListener;

import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.plugins.workflow.modules.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.workflow.modules.unittree.util.ChangeUnitEvent;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.IResourceUserDAO;
import fr.paris.lutece.plugins.workflow.service.WorkflowPlugin;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;

/**
 * Event handler for {@link ChangeUnitEvent}
 */
public class ChangeUnitEventHandler implements ApplicationListener<ChangeUnitEvent> {

	@Inject
	private IResourceUserDAO resourceUserDAO;
	
	@Override
	public void onApplicationEvent(ChangeUnitEvent event) {
		final Plugin plugin = PluginService.getPlugin( WorkflowPlugin.PLUGIN_NAME );
		for ( UnitAssignment assignment : event.getOldAssignmentList( ) )
		{
			List<AdminUser> userList = 
					resourceUserDAO.selectUserListByResource( assignment.getIdResource( ), assignment.getResourceType( ), plugin );
			
			List<Integer> userIdList = UnitHome.findIdsUser( assignment.getIdAssignedUnit( ) );
			
			for ( AdminUser user : userList )
			{
				if ( userIdList.contains( user.getUserId( ) ) )
				{
					resourceUserDAO.deactivateByUserResource( user.getUserId( ), assignment.getIdResource( ), assignment.getResourceType( ), plugin );
				}
			}
			
		}
	}

}
