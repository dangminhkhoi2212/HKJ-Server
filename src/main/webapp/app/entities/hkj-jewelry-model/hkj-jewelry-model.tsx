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

import { getEntities } from './hkj-jewelry-model.reducer';

export const HkjJewelryModel = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const hkjJewelryModelList = useAppSelector(state => state.hkjJewelryModel.entities);
  const loading = useAppSelector(state => state.hkjJewelryModel.loading);
  const totalItems = useAppSelector(state => state.hkjJewelryModel.totalItems);

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
      <h2 id="hkj-jewelry-model-heading" data-cy="HkjJewelryModelHeading">
        <Translate contentKey="serverApp.hkjJewelryModel.home.title">Hkj Jewelry Models</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="serverApp.hkjJewelryModel.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/hkj-jewelry-model/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serverApp.hkjJewelryModel.home.createLabel">Create new Hkj Jewelry Model</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {hkjJewelryModelList && hkjJewelryModelList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('isCustom')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.isCustom">Is Custom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isCustom')} />
                </th>
                <th className="hand" onClick={sort('weight')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.weight">Weight</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('weight')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th className="hand" onClick={sort('color')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.color">Color</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('color')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="serverApp.hkjJewelryModel.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th>
                  <Translate contentKey="serverApp.hkjJewelryModel.project">Project</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {hkjJewelryModelList.map((hkjJewelryModel, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/hkj-jewelry-model/${hkjJewelryModel.id}`} color="link" size="sm">
                      {hkjJewelryModel.id}
                    </Button>
                  </td>
                  <td>{hkjJewelryModel.name}</td>
                  <td>{hkjJewelryModel.description}</td>
                  <td>{hkjJewelryModel.isCustom ? 'true' : 'false'}</td>
                  <td>{hkjJewelryModel.weight}</td>
                  <td>{hkjJewelryModel.price}</td>
                  <td>{hkjJewelryModel.color}</td>
                  <td>{hkjJewelryModel.notes}</td>
                  <td>{hkjJewelryModel.isDeleted ? 'true' : 'false'}</td>
                  <td>{hkjJewelryModel.active ? 'true' : 'false'}</td>
                  <td>{hkjJewelryModel.createdBy}</td>
                  <td>
                    {hkjJewelryModel.createdDate ? (
                      <TextFormat type="date" value={hkjJewelryModel.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{hkjJewelryModel.lastModifiedBy}</td>
                  <td>
                    {hkjJewelryModel.lastModifiedDate ? (
                      <TextFormat type="date" value={hkjJewelryModel.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {hkjJewelryModel.project ? (
                      <Link to={`/hkj-project/${hkjJewelryModel.project.id}`}>{hkjJewelryModel.project.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/hkj-jewelry-model/${hkjJewelryModel.id}`}
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
                        to={`/hkj-jewelry-model/${hkjJewelryModel.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/hkj-jewelry-model/${hkjJewelryModel.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="serverApp.hkjJewelryModel.home.notFound">No Hkj Jewelry Models found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={hkjJewelryModelList && hkjJewelryModelList.length > 0 ? '' : 'd-none'}>
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

export default HkjJewelryModel;
