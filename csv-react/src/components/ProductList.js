import { Table, Popconfirm, Button } from 'antd';

const ProductList = ({ onDelete, products }) => {
    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
        },
        {
            title: 'Action',
            render: (text, record) => {
                return (
                    <Popconfirm title="Delete?" onConfirm={() => onDelete(record.id)}>
                        <Button>Delete</Button>
                    </Popconfirm>
                );
            }
        },
    ];

    return <Table dataSource={products} columns={columns}></Table>
};

export default ProductList;