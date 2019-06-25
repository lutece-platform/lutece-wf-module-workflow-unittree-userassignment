package fr.paris.lutece.plugins.workflow.modules.unittreeuserassignment.business;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignment;
import fr.paris.lutece.plugins.unittree.business.assignment.UnitAssignmentHome;
import fr.paris.lutece.plugins.unittree.business.unit.UnitHome;
import fr.paris.lutece.plugins.workflow.modules.userassignment.business.IAdminUserListProvider;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;

/**
 * An implementation of {@link IAdminUserListProvider} which returns the users of an unit.
 */
public class UnittreeAdminUserListProvider implements IAdminUserListProvider {

	private static final String MESSAGE_LABEL = "module.workflow.unittreeuserassignment.task.user.assign.config.unittree";
	
	private static final String NAME = "unittree";
	
	@Override
	public String getLabelKey( )
	{
		return MESSAGE_LABEL;
	}

	@Override
	public String getName( )
	{
		return NAME;
	}

	@Override
	public List<AdminUser> getUserList( HttpServletRequest request, int resourceId, String resourceType )
	{
		List<AdminUser> userList = new ArrayList<>( );
		
		List<UnitAssignment> assignmentList = UnitAssignmentHome.findByResource( resourceId, resourceType );
		for ( UnitAssignment assignment : assignmentList )
		{
			if ( assignment.isActive( ) )
			{
				List<Integer> userIds = UnitHome.findIdsUser( assignment.getAssignedUnit( ).getIdUnit( ) );
				for ( Integer userId : userIds ) 
				{
					userList.add(AdminUserHome.findByPrimaryKey( userId ) );
				}
			}
		}
		return userList;
	}
}
