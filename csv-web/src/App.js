import React from 'react';
import { BrowserRouter as Router, Route, Link } from 'react-router-dom';
import { Menu, Icon } from 'antd';

import Upload from './csv/Upload';
import List from './csv/List';
import Edit from './csv/Edit';

import './App.css';

const { SubMenu } = Menu;

class App extends React.Component {

  state = {
    current: 'upload',
  };

  handleClick = e => {
    console.log('click ', e);
    this.setState({
      current: e.key,
    });
  };

  render() {
    return (
      <Router>
        <Menu onClick={this.handleClick} selectedKeys={[this.state.current]} mode="horizontal">
          <SubMenu
            title={
              <span className="submenu-title-wrapper">
                <Icon type="upload" />
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

        <Route path='/csv/upload' component={Upload}></Route>
        <Route path='/csv/list' component={List}></Route>
        <Route path='/csv/edit' component={Edit}></Route>
      </Router >
    );
  }
}

export default App;
