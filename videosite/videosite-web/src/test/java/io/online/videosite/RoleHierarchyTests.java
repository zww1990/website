package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleHierarchyTests {
    @Test
    public void testGetReachableGrantedAuthorities() {
        try {
            System.err.println(RoleHierarchyImpl
                    .fromHierarchy("ROLE_ADMIN > ROLE_NORMAL")
                    .getReachableGrantedAuthorities(AuthorityUtils.NO_AUTHORITIES));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRoleHierarchyFromMap() {
        try {
            Map<String, List<String>> map = new HashMap<>();
            map.put("ROLE_ADMIN", List.of("ROLE_NORMAL"));
            System.err.println(RoleHierarchyUtils.roleHierarchyFromMap(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
