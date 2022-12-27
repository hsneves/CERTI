package br.com.hsneves.certi.test.builder;

import org.junit.Assert;
import org.junit.Test;

import br.com.hsneves.certi.test.entity.User;
import br.com.hsneves.certi.test.enums.UserRole;
import br.com.hsneves.certi.test.exceptions.ObjBuilderException;
import br.com.hsneves.certi.test.obj.builder.UserBuilder;

/**
 * Testes unitários para construtor de objetos {@link User}
 * 
 * @author Henrique Neves
 *
 */
public class UserBuilderTest {

	@Test
	public void testBuild() {

		String name = "Henrique";
		boolean active = true;
		String rawPassword = "qwe@123";
		UserRole role = UserRole.ADMIN;

		User user = new UserBuilder().withName(name).withActive(active).withRawPassword(rawPassword).withRole(role).build();

		Assert.assertNotNull(user);
		Assert.assertEquals(user.getName(), name);
		Assert.assertEquals(user.isActive(), active);
		Assert.assertEquals(user.getRawPassword(), rawPassword);
		Assert.assertEquals(user.getRole(), role);

	}

	@Test(expected = ObjBuilderException.class)
	public void testBuildNull() {
		new UserBuilder().build();
	}

	@Test(expected = ObjBuilderException.class)
	public void testBuildRoleNull() {
		new UserBuilder().withName("name").withRawPassword("pwd").build();
	}

	@Test(expected = ObjBuilderException.class)
	public void testBuildNameNull() {
		new UserBuilder().withRole(UserRole.ADMIN).withRawPassword("pwd").build();
	}

	@Test(expected = ObjBuilderException.class)
	public void testBuildPwdNull() {
		new UserBuilder().withName("name").withRole(UserRole.ADMIN).build();
	}
}
