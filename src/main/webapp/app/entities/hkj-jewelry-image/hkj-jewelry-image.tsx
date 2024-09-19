import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './hkj-jewelry-image.reducer';

export const HkjJewelryImage = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const hkjJewelryImageList = useAppSelector(state => state.hkjJewelryImage.entities);
  const loading = useAppSelector(state => state.hkjJewelryImage.loading);
  const totalItems = useAppSelector(state => state.hkjJewelryImage.totalItems);

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
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="hkj-jewelry-image-heading" data-cy="HkjJewelryImageHeading">
        <Translate contentKey="serverApp.hkjJewelryImage.home.title">Hkj Jewelry Images</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="serverApp.hkjJewelryImage.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/hkj-jewelry-image/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serverApp.hkjJewelryImage.home.createLabel">Create new Hkj Jewelry Image</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {hkjJewelryImageList && hkjJewelryImageList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('url')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.url">Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('url')} />
                </th>
                <th className="hand" onClick={sort('isSearchImage')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.isSearchImage">Is Search Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isSearchImage')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('tags')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.tags">Tags</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tags')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="serverApp.hkjJewelryImage.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th>
                  <Translate contentKey="serverApp.hkjJewelryImage.jewelryModel">Jewelry Model</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {hkjJewelryImageList.map((hkjJewelryImage, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/hkj-jewelry-image/${hkjJewelryImage.id}`} color="link" size="sm">
                      {hkjJewelryImage.id}
                    </Button>
                  </td>
                  <td>{hkjJewelryImage.url}</td>
                  <td>{hkjJewelryImage.isSearchImage ? 'true' : 'false'}</td>
                  <td>{hkjJewelryImage.description}</td>
                  <td>{hkjJewelryImage.tags}</td>
                  <td>{hkjJewelryImage.isDeleted ? 'true' : 'false'}</td>
                  <td>{hkjJewelryImage.createdBy}</td>
                  <td>
                    {hkjJewelryImage.createdDate ? (
                      <TextFormat type="date" value={hkjJewelryImage.createdDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{hkjJewelryImage.lastModifiedBy}</td>
                  <td>
                    {hkjJewelryImage.lastModifiedDate ? (
                      <TextFormat type="date" value={hkjJewelryImage.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {hkjJewelryImage.jewelryModel ? (
                      <Link to={`/hkj-jewelry-model/${hkjJewelryImage.jewelryModel.id}`}>{hkjJewelryImage.jewelryModel.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/hkj-jewelry-image/${hkjJewelryImage.id}`}
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
                        to={`/hkj-jewelry-image/${hkjJewelryImage.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/hkj-jewelry-image/${hkjJewelryImage.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="serverApp.hkjJewelryImage.home.notFound">No Hkj Jewelry Images found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={hkjJewelryImageList && hkjJewelryImageList.length > 0 ? '' : 'd-none'}>
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

export default HkjJewelryImage;
