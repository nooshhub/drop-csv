import React from 'react';
import { BrowserRouter as Router, Route, Link } from 'react-router-dom';
import { Layout, Breadcrumb, Menu, Icon } from 'antd';

import Upload from './csv/Upload';
import List from './csv/List';
import Edit from './csv/Edit';

import './App.css';
import TodoApp from './todo/TodoApp';

const { Header, Content, Footer, Sider } = Layout;
const { SubMenu } = Menu;

class App extends React.Component {

  state = {
    current: 'upload',
    collapsed: false,
  };

  handleClick = e => {
    console.log('click ', e);
    this.setState({
      current: e.key,
    });
  };

  onCollapse = collapsed => {
    console.log(collapsed);
    this.setState({ collapsed });
  };

  render() {
    return (
      <Router>

        <Layout style={{ minHeight: '100vh' }}>

          <Sider collapsible collapsed={this.state.collapsed} onCollapse={this.onCollapse}>
            <div className="logo" />
            <Menu theme="dark" onClick={this.handleClick} selectedKeys={[this.state.current]} mode="inline">
              <Menu.Item key="todo">
                <span>
                  <Icon type="desktop" />
                  <Link to='/todo'>Todo</Link>
                </span>
              </Menu.Item>
              <SubMenu
                title={
                  <span className="submenu-title-wrapper">
                    <Icon type="file" />
                    <Link to='/'>CSV</Link>
                  </span>
                }
              >
                <Menu.Item key="upload:1">
                  <Link to='/csv/upload'>Upload</Link>
                </Menu.Item>
                <Menu.Item key="upload:2">
                  <Link to='/csv/list'>List</Link>
                </Menu.Item>
              </SubMenu>
            </Menu>
          </Sider>

          <Layout>

            <Header style={{ background: '#fff', padding: 0 }} >
              <Breadcrumb style={{ margin: '16px 0 0 10px' }}>
                <Breadcrumb.Item>CSV</Breadcrumb.Item>
                <Breadcrumb.Item>Upload</Breadcrumb.Item>
              </Breadcrumb>
            </Header>

            <Content style={{ margin: '0 16px' }}>
              <div style={{ padding: 24, background: '#fff' }}>
                <Route path='/csv/upload' component={Upload}></Route>
                <Route path='/csv/list' component={List}></Route>
                <Route path='/csv/edit/:id' component={Edit}></Route>
                <Route path='/todo' component={TodoApp}></Route>
              </div>
            </Content>

            <Footer style={{ textAlign: 'center' }}>CSV Design ©2019 Created by CSV</Footer>

          </Layout>

        </Layout>

      </Router >
    );
  }
}

export default App;
