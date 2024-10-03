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

import { getEntities } from './hkj-task.reducer';

export const HkjTask = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const hkjTaskList = useAppSelector(state => state.hkjTask.entities);
  const loading = useAppSelector(state => state.hkjTask.loading);
  const totalItems = useAppSelector(state => state.hkjTask.totalItems);

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
      <h2 id="hkj-task-heading" data-cy="HkjTaskHeading">
        <Translate contentKey="serverApp.hkjTask.home.title">Hkj Tasks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="serverApp.hkjTask.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/hkj-task/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serverApp.hkjTask.home.createLabel">Create new Hkj Task</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {hkjTaskList && hkjTaskList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="serverApp.hkjTask.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="serverApp.hkjTask.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('coverImage')}>
                  <Translate contentKey="serverApp.hkjTask.coverImage">Cover Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('coverImage')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="serverApp.hkjTask.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('assignedDate')}>
                  <Translate contentKey="serverApp.hkjTask.assignedDate">Assigned Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('assignedDate')} />
                </th>
                <th className="hand" onClick={sort('expectDate')}>
                  <Translate contentKey="serverApp.hkjTask.expectDate">Expect Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expectDate')} />
                </th>
                <th className="hand" onClick={sort('completedDate')}>
                  <Translate contentKey="serverApp.hkjTask.completedDate">Completed Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('completedDate')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="serverApp.hkjTask.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('priority')}>
                  <Translate contentKey="serverApp.hkjTask.priority">Priority</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('priority')} />
                </th>
                <th className="hand" onClick={sort('point')}>
                  <Translate contentKey="serverApp.hkjTask.point">Point</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('point')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="serverApp.hkjTask.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th className="hand" onClick={sort('isDeleted')}>
                  <Translate contentKey="serverApp.hkjTask.isDeleted">Is Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isDeleted')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="serverApp.hkjTask.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="serverApp.hkjTask.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="serverApp.hkjTask.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="serverApp.hkjTask.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th>
                  <Translate contentKey="serverApp.hkjTask.employee">Employee</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="serverApp.hkjTask.hkjProject">Hkj Project</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {hkjTaskList.map((hkjTask, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/hkj-task/${hkjTask.id}`} color="link" size="sm">
                      {hkjTask.id}
                    </Button>
                  </td>
                  <td>{hkjTask.name}</td>
                  <td>{hkjTask.coverImage}</td>
                  <td>{hkjTask.description}</td>
                  <td>{hkjTask.assignedDate ? <TextFormat type="date" value={hkjTask.assignedDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{hkjTask.expectDate ? <TextFormat type="date" value={hkjTask.expectDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {hkjTask.completedDate ? <TextFormat type="date" value={hkjTask.completedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    <Translate contentKey={`serverApp.HkjOrderStatus.${hkjTask.status}`} />
                  </td>
                  <td>
                    <Translate contentKey={`serverApp.HkjPriority.${hkjTask.priority}`} />
                  </td>
                  <td>{hkjTask.point}</td>
                  <td>{hkjTask.notes}</td>
                  <td>{hkjTask.isDeleted ? 'true' : 'false'}</td>
                  <td>{hkjTask.createdBy}</td>
                  <td>{hkjTask.createdDate ? <TextFormat type="date" value={hkjTask.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{hkjTask.lastModifiedBy}</td>
                  <td>
                    {hkjTask.lastModifiedDate ? <TextFormat type="date" value={hkjTask.lastModifiedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{hkjTask.employee ? <Link to={`/user-extra/${hkjTask.employee.id}`}>{hkjTask.employee.id}</Link> : ''}</td>
                  <td>{hkjTask.hkjProject ? <Link to={`/hkj-project/${hkjTask.hkjProject.id}`}>{hkjTask.hkjProject.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/hkj-task/${hkjTask.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/hkj-task/${hkjTask.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/hkj-task/${hkjTask.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="serverApp.hkjTask.home.notFound">No Hkj Tasks found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={hkjTaskList && hkjTaskList.length > 0 ? '' : 'd-none'}>
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

export default HkjTask;
