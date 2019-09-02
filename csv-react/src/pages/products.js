
import styles from './products.css';
import { connect } from 'dva';
import ProductList from '../components/ProductList';
import router from 'umi/router';

const Products = ({ dispatch, products }) => {
  function handleDelete(id) {
    dispatch({
      type: 'products/delete',
      payload: id,
    });
  }
  return (
    <div>
      <h2>List of Products</h2>
      <button className="primary" onClick={() => { router.goBack(); }}>Go Back</button>
      <ProductList onDelete={handleDelete} products={products}></ProductList>
    </div>
  );
}

export default connect(({ products }) => ({
  products
}))(Products);