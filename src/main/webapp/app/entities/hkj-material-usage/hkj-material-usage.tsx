import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './hkj-material-usage.reducer';

export const HkjMaterialUsage = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const hkjMaterialUsageList = useAppSelector(state => state.hkjMaterialUsage.entities);
  const loading = useAppSelector(state => state.hkjMaterialUsage.loading);
  const totalItems = useAppSelector(state => state.hkjMaterialUsage.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="hkj-material-usage-heading" data-cy="HkjMaterialUsageHeading">
        <Translate contentKey="serverApp.hkjMaterialUsage.home.title">Hkj Material Usages</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="serverApp.hkjMaterialUsage.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/hkj-material-usage/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serverApp.hkjMaterialUsage.home.createLabel">Create new Hkj Material Usage</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {hkjMaterialUsageList && hkjMaterialUsageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('quantity')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.quantity">Quantity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('quantity')} />
                </th>
                <th className="hand" onClick={sort('lossQuantity')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.lossQuantity">Loss Quantity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lossQuantity')} />
                </th>
                <th className="hand" onClick={sort('usageDate')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.usageDate">Usage Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('usageDate')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th className="hand" onClick={sort('weight')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.weight">Weight</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('weight')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="serverApp.hkjMaterialUsage.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th>
                  <Translate contentKey="serverApp.hkjMaterialUsage.material">Material</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="serverApp.hkjMaterialUsage.task">Task</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {hkjMaterialUsageList.map((hkjMaterialUsage, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/hkj-material-usage/${hkjMaterialUsage.id}`} color="link" size="sm">
                      {hkjMaterialUsage.id}
                    </Button>
                  </td>
                  <td>{hkjMaterialUsage.quantity}</td>
                  <td>{hkjMaterialUsage.lossQuantity}</td>
                  <td>
                    {hkjMaterialUsage.usageDate ? (
                      <TextFormat type="date" value={hkjMaterialUsage.usageDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{hkjMaterialUsage.notes}</td>
                  <td>{hkjMaterialUsage.weight}</td>
                  <td>{hkjMaterialUsage.price}</td>
                  <td>{hkjMaterialUsage.isDeleted ? 'true' : 'false'}</td>
                  <td>{hkjMaterialUsage.createdBy}</td>
                  <td>
                    {hkjMaterialUsage.createdDate ? (
                      <TextFormat type="date" value={hkjMaterialUsage.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{hkjMaterialUsage.lastModifiedBy}</td>
                  <td>
                    {hkjMaterialUsage.lastModifiedDate ? (
                      <TextFormat type="date" value={hkjMaterialUsage.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {hkjMaterialUsage.material ? (
                      <Link to={`/hkj-material/${hkjMaterialUsage.material.id}`}>{hkjMaterialUsage.material.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {hkjMaterialUsage.task ? <Link to={`/hkj-task/${hkjMaterialUsage.task.id}`}>{hkjMaterialUsage.task.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/hkj-material-usage/${hkjMaterialUsage.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/hkj-material-usage/${hkjMaterialUsage.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/hkj-material-usage/${hkjMaterialUsage.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="serverApp.hkjMaterialUsage.home.notFound">No Hkj Material Usages found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={hkjMaterialUsageList && hkjMaterialUsageList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default HkjMaterialUsage;
