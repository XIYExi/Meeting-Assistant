-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议聊天', '2000', '1', 'chat', 'meeting/chat/index', 1, 0, 'C', '0', '0', 'meeting:chat:list', '#', 'admin', sysdate(), '', null, '会议聊天菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议聊天查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'meeting:chat:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议聊天新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'meeting:chat:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议聊天修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'meeting:chat:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议聊天删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'meeting:chat:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会议聊天导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'meeting:chat:export',       '#', 'admin', sysdate(), '', null, '');