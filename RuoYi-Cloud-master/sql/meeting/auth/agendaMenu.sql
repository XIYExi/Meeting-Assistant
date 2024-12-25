-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议议程', '2000', '1', 'agenda', 'meeting/agenda/index', 1, 0, 'C', '0', '0', 'meeting:agenda:list', '#', 'admin', sysdate(), '', null, '会议议程菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议议程查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'meeting:agenda:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议议程新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'meeting:agenda:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议议程修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'meeting:agenda:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议议程删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'meeting:agenda:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议议程导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'meeting:agenda:export',       '#', 'admin', sysdate(), '', null, '');